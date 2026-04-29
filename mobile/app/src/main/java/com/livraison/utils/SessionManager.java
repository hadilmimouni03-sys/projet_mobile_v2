package com.livraison.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "LivraisonSession";
    private static final String KEY_TOKEN    = "token";
    private static final String KEY_IDPERS   = "idpers";
    private static final String KEY_NOM      = "nom";
    private static final String KEY_PRENOM   = "prenom";
    private static final String KEY_ROLE     = "role";
    private static final String KEY_LOGIN    = "login";

    private final SharedPreferences prefs;

    public SessionManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(String token, Integer idpers, String nom,
                            String prenom, String role, String login) {
        prefs.edit()
             .putString(KEY_TOKEN, token)
             .putInt(KEY_IDPERS, idpers)
             .putString(KEY_NOM, nom)
             .putString(KEY_PRENOM, prenom)
             .putString(KEY_ROLE, role)
             .putString(KEY_LOGIN, login)
             .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return prefs.getString(KEY_TOKEN, null) != null;
    }

    public String getToken()  { return prefs.getString(KEY_TOKEN, ""); }
    public int    getIdpers() { return prefs.getInt(KEY_IDPERS, -1); }
    public String getNom()    { return prefs.getString(KEY_NOM, ""); }
    public String getPrenom() { return prefs.getString(KEY_PRENOM, ""); }
    public String getRole()   { return prefs.getString(KEY_ROLE, ""); }
    public String getLogin()  { return prefs.getString(KEY_LOGIN, ""); }

    public boolean isControleur() { return "CTR".equals(getRole()); }
    public boolean isLivreur()    { return "LIV".equals(getRole()); }

    public String getBearerToken() { return "Bearer " + getToken(); }
}
