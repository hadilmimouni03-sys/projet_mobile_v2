package com.livraison.controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.livraison.LoginActivity;
import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityControleurDashboardBinding;
import com.livraison.model.DashboardStats;
import com.livraison.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControleurDashboardActivity extends AppCompatActivity {

    private ActivityControleurDashboardBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControleurDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        binding.tvBienvenue.setText("Bonjour, " + session.getNom() + " " + session.getPrenom());

        loadDashboard();

        binding.btnLivraisons.setOnClickListener(v ->
                startActivity(new Intent(this, ControleurLivraisonsActivity.class)));
        binding.btnRecherche.setOnClickListener(v ->
                startActivity(new Intent(this, ControleurRechercheActivity.class)));
        binding.btnMessages.setOnClickListener(v ->
                startActivity(new Intent(this, ControleurMessagesActivity.class)));
        binding.btnDeconnexion.setOnClickListener(v -> logout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboard();
    }

    private void loadDashboard() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi()
                .getDashboard(session.getBearerToken(), null, null)
                .enqueue(new Callback<DashboardStats>() {
                    @Override
                    public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            displayStats(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<DashboardStats> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ControleurDashboardActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayStats(DashboardStats stats) {
        binding.tvTotal.setText(String.valueOf(stats.totalLivraisons));
        binding.tvLivre.setText(String.valueOf(stats.livre));
        binding.tvNonLivre.setText(String.valueOf(stats.nonLivre));
        binding.tvEnCours.setText(String.valueOf(stats.enCours));
        binding.tvEnAttente.setText(String.valueOf(stats.enAttente));

        // Stats par livreur
        StringBuilder sbLivreur = new StringBuilder();
        if (stats.parLivreur != null) {
            for (DashboardStats.StatLivreur sl : stats.parLivreur) {
                sbLivreur.append("• ").append(sl.nomLivreur)
                         .append(" : ").append(sl.total).append(" livraison(s)\n");
                if (sl.parEtat != null) {
                    sl.parEtat.forEach((etat, nb) ->
                            sbLivreur.append("   - ").append(etatLabel(etat))
                                     .append(" : ").append(nb.intValue()).append("\n"));
                }
            }
        }
        binding.tvStatsLivreur.setText(sbLivreur.length() > 0 ?
                sbLivreur.toString() : "Aucune donnée");

        // Stats par client
        StringBuilder sbClient = new StringBuilder();
        if (stats.parClient != null) {
            for (DashboardStats.StatClient sc : stats.parClient) {
                sbClient.append("• ").append(sc.nomClient)
                        .append(" : ").append(sc.total).append(" commande(s)\n");
            }
        }
        binding.tvStatsClient.setText(sbClient.length() > 0 ?
                sbClient.toString() : "Aucune donnée");
    }

    private String etatLabel(String etat) {
        switch (etat) {
            case "LIVRE":      return "Livré";
            case "NON_LIVRE":  return "Non livré";
            case "EN_COURS":   return "En cours";
            case "EN_ATTENTE": return "En attente";
            default:           return etat;
        }
    }

    private void logout() {
        session.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
