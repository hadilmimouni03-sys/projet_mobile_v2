package com.livraison.controleur;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.livraison.api.RetrofitClient;
import com.livraison.databinding.ActivityEnvoyerMessageBinding;
import com.livraison.model.Livreur;
import com.livraison.model.Message;
import com.livraison.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnvoyerMessageActivity extends AppCompatActivity {

    private ActivityEnvoyerMessageBinding binding;
    private SessionManager session;
    private List<Livreur> livreurs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnvoyerMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadLivreurs();

        binding.btnEnvoyer.setOnClickListener(v -> envoyer());
    }

    private void loadLivreurs() {
        RetrofitClient.getInstance().getApi()
                .getLivreurs(session.getBearerToken())
                .enqueue(new Callback<List<Livreur>>() {
                    @Override
                    public void onResponse(Call<List<Livreur>> call, Response<List<Livreur>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            livreurs.clear();
                            livreurs.addAll(resp.body());
                            List<String> noms = new ArrayList<>();
                            for (Livreur l : livreurs) noms.add(l.getNomComplet());
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    EnvoyerMessageActivity.this,
                                    android.R.layout.simple_spinner_item, noms);
                            adapter.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerLivreur.setAdapter(adapter);
                        }
                    }
                    @Override public void onFailure(Call<List<Livreur>> call, Throwable t) {}
                });
    }

    private void envoyer() {
        String contenu = binding.etContenu.getText().toString().trim();
        if (contenu.isEmpty()) {
            Toast.makeText(this, "Le message est vide", Toast.LENGTH_SHORT).show();
            return;
        }
        if (livreurs.isEmpty()) {
            Toast.makeText(this, "Aucun livreur disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        int pos = binding.spinnerLivreur.getSelectedItemPosition();
        Livreur dest = livreurs.get(pos);

        String nocdeStr = binding.etNocde.getText().toString().trim();

        Map<String, Object> body = new HashMap<>();
        body.put("destinataireId", dest.idpers);
        body.put("contenu", contenu);
        body.put("typeMessage", "INFO");
        if (!nocdeStr.isEmpty()) body.put("nocde", Integer.parseInt(nocdeStr));

        binding.btnEnvoyer.setEnabled(false);
        RetrofitClient.getInstance().getApi()
                .envoyerMessage(session.getBearerToken(), body)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> resp) {
                        binding.btnEnvoyer.setEnabled(true);
                        if (resp.isSuccessful()) {
                            Toast.makeText(EnvoyerMessageActivity.this,
                                    "Message envoyé", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EnvoyerMessageActivity.this,
                                    "Erreur d'envoi", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        binding.btnEnvoyer.setEnabled(true);
                        Toast.makeText(EnvoyerMessageActivity.this,
                                "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
