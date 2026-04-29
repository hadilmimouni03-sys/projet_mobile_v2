package com.livraison.controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.livraison.adapter.LivraisonAdapter;
import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityControleurLivraisonsBinding;
import com.livraison.livreur.LivreurDetailActivity;
import com.livraison.model.Livraison;
import com.livraison.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControleurLivraisonsActivity extends AppCompatActivity {

    private ActivityControleurLivraisonsBinding binding;
    private SessionManager session;
    private LivraisonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControleurLivraisonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new LivraisonAdapter(livraison -> {
            Intent intent = new Intent(this, LivreurDetailActivity.class);
            intent.putExtra("nocde", livraison.nocde);
            intent.putExtra("readOnly", true);
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.btnAujourdhui.setOnClickListener(v -> loadAujourdhui());
        binding.btnPeriode.setOnClickListener(v -> loadPeriode());
        binding.swipeRefresh.setOnRefreshListener(() -> loadAujourdhui());

        loadAujourdhui();
    }

    private void loadAujourdhui() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi()
                .getLivraisonsAujourdhui(session.getBearerToken())
                .enqueue(new Callback<List<Livraison>>() {
                    @Override
                    public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> resp) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (resp.isSuccessful() && resp.body() != null) {
                            adapter.setData(resp.body());
                            binding.tvCount.setText(resp.body().size() + " livraison(s)");
                            binding.tvTitre.setText("Livraisons du jour");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Livraison>> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        Toast.makeText(ControleurLivraisonsActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadPeriode() {
        String debut = binding.etDateDebut.getText().toString().trim();
        String fin   = binding.etDateFin.getText().toString().trim();
        if (debut.isEmpty() || fin.isEmpty()) {
            Toast.makeText(this, "Saisir les dates (AAAA-MM-JJ)", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi()
                .getLivraisons(session.getBearerToken(), debut, fin)
                .enqueue(new Callback<List<Livraison>>() {
                    @Override
                    public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> resp) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (resp.isSuccessful() && resp.body() != null) {
                            adapter.setData(resp.body());
                            binding.tvCount.setText(resp.body().size() + " livraison(s)");
                            binding.tvTitre.setText("Période : " + debut + " → " + fin);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Livraison>> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ControleurLivraisonsActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
