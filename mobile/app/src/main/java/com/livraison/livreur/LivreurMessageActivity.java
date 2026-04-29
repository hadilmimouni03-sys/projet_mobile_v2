package com.livraison.livreur;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.livraison.adapter.MessageAdapter;
import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityLivreurMessageBinding;
import com.livraison.model.Message;
import com.livraison.utils.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivreurMessageActivity extends AppCompatActivity {

    private ActivityLivreurMessageBinding binding;
    private SessionManager session;
    private MessageAdapter adapterRecus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLivreurMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterRecus = new MessageAdapter(false);
        binding.recyclerRecus.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerRecus.setAdapter(adapterRecus);

        // Pré-remplir si venant du détail livraison
        int nocde = getIntent().getIntExtra("nocde", -1);
        String telClient = getIntent().getStringExtra("telClient");
        String nomClient = getIntent().getStringExtra("nomClient");

        if (nocde != -1) {
            binding.etNocde.setText(String.valueOf(nocde));
        }
        if (telClient != null && nomClient != null) {
            binding.etContenu.setText("URGENCE – Commande #" + nocde +
                    " | Client : " + nomClient + " | Tél : " + telClient + "\n\n[Motif : ]");
        }

        binding.btnEnvoyerUrgence.setOnClickListener(v -> envoyerUrgence());
        loadMessagesRecus();
    }

    private void envoyerUrgence() {
        String contenu = binding.etContenu.getText().toString().trim();
        if (contenu.isEmpty()) {
            Toast.makeText(this, "Message vide", Toast.LENGTH_SHORT).show();
            return;
        }
        String nocdeStr = binding.etNocde.getText().toString().trim();

        Map<String, Object> body = new HashMap<>();
        body.put("contenu", contenu);
        body.put("typeMessage", "URGENCE");
        if (!nocdeStr.isEmpty()) body.put("nocde", Integer.parseInt(nocdeStr));

        binding.btnEnvoyerUrgence.setEnabled(false);
        RetrofitClient.getInstance().getApi()
                .envoyerMessage(session.getBearerToken(), body)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> resp) {
                        binding.btnEnvoyerUrgence.setEnabled(true);
                        if (resp.isSuccessful()) {
                            Toast.makeText(LivreurMessageActivity.this,
                                    "Message d'urgence envoyé", Toast.LENGTH_SHORT).show();
                            binding.etContenu.setText("");
                            finish();
                        } else {
                            Toast.makeText(LivreurMessageActivity.this,
                                    "Erreur d'envoi", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        binding.btnEnvoyerUrgence.setEnabled(true);
                        Toast.makeText(LivreurMessageActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadMessagesRecus() {
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
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
