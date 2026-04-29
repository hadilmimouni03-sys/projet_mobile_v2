package com.livraison.controleur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.livraison.adapter.LivraisonAdapter;
import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityControleurRechercheBinding;
import com.livraison.livreur.LivreurDetailActivity;
import com.livraison.model.Livraison;
import com.livraison.model.Livreur;
import com.livraison.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControleurRechercheActivity extends AppCompatActivity {

    private ActivityControleurRechercheBinding binding;
    private SessionManager session;
    private LivraisonAdapter adapter;
    private List<Livreur> livreurs = new ArrayList<>();
    private List<String> etatOptions = List.of("Tous", "EN_ATTENTE", "EN_COURS", "LIVRE", "NON_LIVRE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControleurRechercheBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new LivraisonAdapter(liv -> {
            Intent i = new Intent(this, LivreurDetailActivity.class);
            i.putExtra("nocde", liv.nocde);
            i.putExtra("readOnly", true);
            startActivity(i);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Spinner états
        ArrayAdapter<String> etatAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, etatOptions);
        etatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEtat.setAdapter(etatAdapter);

        loadLivreurs();
        binding.btnRechercher.setOnClickListener(v -> rechercher());
    }

    private void loadLivreurs() {
        RetrofitClient.getInstance().getApi()
                .getLivreurs(session.getBearerToken())
                .enqueue(new Callback<List<Livreur>>() {
                    @Override
                    public void onResponse(Call<List<Livreur>> call, Response<List<Livreur>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            livreurs.clear();
                            livreurs.add(null); // option "Tous"
                            livreurs.addAll(resp.body());
                            List<String> noms = new ArrayList<>();
                            noms.add("Tous les livreurs");
                            for (Livreur l : resp.body()) noms.add(l.getNomComplet());
                            ArrayAdapter<String> a = new ArrayAdapter<>(ControleurRechercheActivity.this,
                                    android.R.layout.simple_spinner_item, noms);
                            a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerLivreur.setAdapter(a);
                        }
                    }
                    @Override public void onFailure(Call<List<Livreur>> call, Throwable t) {}
                });
    }

    private void rechercher() {
        String dateStr = binding.etDate.getText().toString().trim();
        String nocdeStr = binding.etNocde.getText().toString().trim();
        String nocltStr = binding.etNoclt.getText().toString().trim();

        String dateliv = dateStr.isEmpty() ? null : dateStr;
        Integer nocde = nocdeStr.isEmpty() ? null : Integer.parseInt(nocdeStr);
        Integer noclt = nocltStr.isEmpty() ? null : Integer.parseInt(nocltStr);

        int livreurPos = binding.spinnerLivreur.getSelectedItemPosition();
        Integer livreurId = (livreurPos > 0 && livreurs.size() > livreurPos && livreurs.get(livreurPos) != null)
                ? livreurs.get(livreurPos).idpers : null;

        int etatPos = binding.spinnerEtat.getSelectedItemPosition();
        String etatliv = etatPos == 0 ? null : etatOptions.get(etatPos);

        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi()
                .rechercher(session.getBearerToken(), dateliv, livreurId, etatliv, noclt, nocde)
                .enqueue(new Callback<List<Livraison>>() {
                    @Override
                    public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> resp) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (resp.isSuccessful() && resp.body() != null) {
                            adapter.setData(resp.body());
                            binding.tvCount.setText(resp.body().size() + " résultat(s)");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Livraison>> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ControleurRechercheActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
