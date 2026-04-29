package com.livraison.livreur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.livraison.LoginActivity;
import com.livraison.adapter.LivraisonAdapter;
import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityLivreurLivraisonsBinding;
import com.livraison.model.Livraison;
import com.livraison.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivreurLivraisonsActivity extends AppCompatActivity {

    private ActivityLivreurLivraisonsBinding binding;
    private SessionManager session;
    private LivraisonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLivreurLivraisonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        binding.tvBienvenue.setText("Bonjour, " + session.getNom() + " " + session.getPrenom());

        adapter = new LivraisonAdapter(liv -> {
            Intent i = new Intent(this, LivreurDetailActivity.class);
            i.putExtra("nocde", liv.nocde);
            i.putExtra("readOnly", false);
            startActivity(i);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadMesLivraisons);
        binding.btnMessages.setOnClickListener(v ->
                startActivity(new Intent(this, LivreurMessageActivity.class)));
        binding.btnDeconnexion.setOnClickListener(v -> logout());

        loadMesLivraisons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMesLivraisons();
    }

    private void loadMesLivraisons() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi()
                .getMesLivraisons(session.getBearerToken())
                .enqueue(new Callback<List<Livraison>>() {
                    @Override
                    public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> resp) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (resp.isSuccessful() && resp.body() != null) {
                            List<Livraison> data = resp.body();
                            adapter.setData(data);
                            binding.tvCount.setText(data.size() + " livraison(s) aujourd'hui");
                            if (data.isEmpty()) {
                                binding.tvVide.setVisibility(View.VISIBLE);
                                binding.recyclerView.setVisibility(View.GONE);
                            } else {
                                binding.tvVide.setVisibility(View.GONE);
                                binding.recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Livraison>> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        Toast.makeText(LivreurLivraisonsActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logout() {
        session.clear();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
