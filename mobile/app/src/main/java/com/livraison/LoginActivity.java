package com.livraison;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.livraison.api.RetrofitClient;
import com.livraison.controleur.ControleurDashboardActivity;
import com.livraison.databinding.ActivityLoginBinding;
import com.livraison.livreur.LivreurLivraisonsActivity;
import com.livraison.model.User;
import com.livraison.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);

        if (session.isLoggedIn()) {
            navigateByRole(session.getRole());
        }

        binding.btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String login = binding.etLogin.getText().toString().trim();
        String motP  = binding.etPassword.getText().toString().trim();

        if (login.isEmpty() || motP.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        Map<String, String> creds = new HashMap<>();
        creds.put("login", login);
        creds.put("motP", motP);

        RetrofitClient.getInstance().getApi().login(creds)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnLogin.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            User u = response.body();
                            session.saveSession(u.token, u.idpers, u.nom, u.prenom, u.role, u.login);
                            navigateByRole(u.role);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Identifiants incorrects", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this,
                                "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void navigateByRole(String role) {
        Intent intent;
        if ("CTR".equals(role)) {
            intent = new Intent(this, ControleurDashboardActivity.class);
        } else {
            intent = new Intent(this, LivreurLivraisonsActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
