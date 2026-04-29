package com.livraison.controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.livraison.adapter.MessageAdapter;
import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityControleurMessagesBinding;
import com.livraison.model.Message;
import com.livraison.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControleurMessagesActivity extends AppCompatActivity {

    private ActivityControleurMessagesBinding binding;
    private SessionManager session;
    private MessageAdapter adapterRecus;
    private MessageAdapter adapterUrgence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControleurMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterRecus = new MessageAdapter(false);
        adapterUrgence = new MessageAdapter(false);

        binding.recyclerRecus.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerRecus.setAdapter(adapterRecus);

        binding.recyclerUrgence.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerUrgence.setAdapter(adapterUrgence);

        binding.btnEnvoyerMessage.setOnClickListener(v ->
                startActivity(new Intent(this, EnvoyerMessageActivity.class)));

        loadMessages();
    }

    @Override
    protected void onResume() { super.onResume(); loadMessages(); }

    private void loadMessages() {
        binding.progressBar.setVisibility(View.VISIBLE);

        // Messages reçus
        RetrofitClient.getInstance().getApi()
                .getMessagesRecus(session.getBearerToken())
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(Call<List<Message>> call, Response<List<Message>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            adapterRecus.setData(resp.body());
                        }
                    }
                    @Override public void onFailure(Call<List<Message>> call, Throwable t) {}
                });

        // Messages d'urgence
        RetrofitClient.getInstance().getApi()
                .getMessagesUrgence(session.getBearerToken())
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(Call<List<Message>> call, Response<List<Message>> resp) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (resp.isSuccessful() && resp.body() != null) {
                            adapterUrgence.setData(resp.body());
                            long nonLus = resp.body().stream()
                                    .filter(m -> Boolean.FALSE.equals(m.lu)).count();
                            if (nonLus > 0) {
                                binding.tvUrgenceBadge.setText(nonLus + " non lu(s)");
                                binding.tvUrgenceBadge.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Message>> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ControleurMessagesActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
