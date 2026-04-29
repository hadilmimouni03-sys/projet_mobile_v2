package com.livraison.livreur;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityLivreurDetailBinding;
import com.livraison.model.Livraison;
import com.livraison.utils.SessionManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivreurDetailActivity extends AppCompatActivity {

    private ActivityLivreurDetailBinding binding;
    private SessionManager session;
    private Integer nocde;
    private boolean readOnly;
    private Livraison livraison;

    private final List<String> etats = Arrays.asList(
            "EN_ATTENTE", "EN_COURS", "LIVRE", "NON_LIVRE", "REPORTE");
    private final List<String> etatsLabels = Arrays.asList(
            "En attente", "En cours", "Livré", "Non livré", "Reporté");
    private final List<String> modesPay = Arrays.asList(
            "ESPECES", "CB", "VIREMENT", "CHEQUE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLivreurDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        nocde = getIntent().getIntExtra("nocde", -1);
        readOnly = getIntent().getBooleanExtra("readOnly", false);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Livraison #" + nocde);

        // Spinners
        ArrayAdapter<String> etatAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, etatsLabels);
        etatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEtat.setAdapter(etatAdapter);

        ArrayAdapter<String> payAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, modesPay);
        payAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerModepay.setAdapter(payAdapter);

        if (readOnly) {
            binding.layoutModification.setVisibility(View.GONE);
        }

        loadDetail();

        binding.btnSauvegarder.setOnClickListener(v -> sauvegarder());
        binding.btnEnvoyerUrgence.setOnClickListener(v -> envoyerUrgence());
    }

    private void loadDetail() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi()
                .getDetailLivraison(session.getBearerToken(), nocde)
                .enqueue(new Callback<Livraison>() {
                    @Override
                    public void onResponse(Call<Livraison> call, Response<Livraison> resp) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (resp.isSuccessful() && resp.body() != null) {
                            livraison = resp.body();
                            afficherDetail(livraison);
                        }
                    }
                    @Override
                    public void onFailure(Call<Livraison> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(LivreurDetailActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void afficherDetail(Livraison l) {
        binding.tvNomClient.setText(l.getNomCompletClient());
        binding.tvTelClient.setText(l.telClient != null ? l.telClient : "N/A");
        binding.tvAdresseClient.setText(
                (l.adresseClient != null ? l.adresseClient : "") + "\n" +
                (l.codePostalClient != null ? l.codePostalClient + " " : "") +
                (l.villeClient != null ? l.villeClient : ""));
        binding.tvEmailClient.setText(l.emailClient != null ? l.emailClient : "N/A");
        binding.tvNocde.setText("Commande #" + l.nocde);
        binding.tvMontant.setText(l.montantTotal != null ?
                String.format("%.2f €", l.montantTotal) : "N/A");
        binding.tvNbArticles.setText(l.nombreArticles != null ?
                l.nombreArticles + " article(s)" : "N/A");
        binding.tvDateLiv.setText(l.dateliv != null ? l.dateliv : "Non planifiée");

        // Remplir les lignes de commande
        if (l.lignes != null) {
            StringBuilder sb = new StringBuilder();
            for (Livraison.Ligne lg : l.lignes) {
                sb.append("• ").append(lg.designation)
                  .append(" x").append(lg.qtecde)
                  .append(" = ").append(String.format("%.2f €", lg.sousTotal)).append("\n");
            }
            binding.tvLignes.setText(sb.toString());
        }

        // Sélectionner l'état actuel dans le spinner
        if (l.etatliv != null) {
            int idx = etats.indexOf(l.etatliv);
            if (idx >= 0) binding.spinnerEtat.setSelection(idx);
        }
        if (l.modepay != null) {
            int idx = modesPay.indexOf(l.modepay);
            if (idx >= 0) binding.spinnerModepay.setSelection(idx);
        }
        if (l.remarques != null) binding.etRemarques.setText(l.remarques);

        // Bouton maps
        binding.btnOuvrirMaps.setOnClickListener(v -> {
            String adresse = (l.adresseClient != null ? l.adresseClient : "") +
                             " " + (l.villeClient != null ? l.villeClient : "");
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=" + Uri.encode(adresse)));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://maps.google.com/?q=" + Uri.encode(adresse))));
            }
        });

        // Bouton appeler
        binding.btnAppeler.setOnClickListener(v -> {
            if (l.telClient != null) {
                startActivity(new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + l.telClient)));
            }
        });
    }

    private void sauvegarder() {
        int etatPos = binding.spinnerEtat.getSelectedItemPosition();
        int payPos  = binding.spinnerModepay.getSelectedItemPosition();
        String etatliv  = etats.get(etatPos);
        String modepay  = modesPay.get(payPos);
        String remarques = binding.etRemarques.getText().toString().trim();

        Map<String, String> body = new HashMap<>();
        body.put("etatliv", etatliv);
        body.put("modepay", modepay);
        if (!remarques.isEmpty()) body.put("remarques", remarques);

        binding.btnSauvegarder.setEnabled(false);
        RetrofitClient.getInstance().getApi()
                .updateLivraison(session.getBearerToken(), nocde, body)
                .enqueue(new Callback<Livraison>() {
                    @Override
                    public void onResponse(Call<Livraison> call, Response<Livraison> resp) {
                        binding.btnSauvegarder.setEnabled(true);
                        if (resp.isSuccessful()) {
                            Toast.makeText(LivreurDetailActivity.this,
                                    "Livraison mise à jour", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LivreurDetailActivity.this,
                                    "Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Livraison> call, Throwable t) {
                        binding.btnSauvegarder.setEnabled(true);
                        Toast.makeText(LivreurDetailActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void envoyerUrgence() {
        Intent i = new Intent(this, LivreurMessageActivity.class);
        i.putExtra("nocde", nocde);
        if (livraison != null) {
            i.putExtra("telClient", livraison.telClient);
            i.putExtra("nomClient", livraison.getNomCompletClient());
        }
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
