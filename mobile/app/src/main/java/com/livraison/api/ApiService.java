package com.livraison.api;

import com.livraison.model.DashboardStats;
import com.livraison.model.Livraison;
import com.livraison.model.Livreur;
import com.livraison.model.Message;
import com.livraison.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // --- Auth ---
    @POST("auth/login")
    Call<User> login(@Body Map<String, String> credentials);

    // --- Livraisons ---
    @GET("livraisons/today")
    Call<List<Livraison>> getLivraisonsAujourdhui(@Header("Authorization") String token);

    @GET("livraisons")
    Call<List<Livraison>> getLivraisons(
            @Header("Authorization") String token,
            @Query("debut") String debut,
            @Query("fin") String fin);

    @GET("livraisons/recherche")
    Call<List<Livraison>> rechercher(
            @Header("Authorization") String token,
            @Query("dateliv")  String dateliv,
            @Query("livreurId") Integer livreurId,
            @Query("etatliv")  String etatliv,
            @Query("noclt")    Integer noclt,
            @Query("nocde")    Integer nocde);

    @GET("livraisons/mes-livraisons")
    Call<List<Livraison>> getMesLivraisons(@Header("Authorization") String token);

    @GET("livraisons/{nocde}")
    Call<Livraison> getDetailLivraison(
            @Header("Authorization") String token,
            @Path("nocde") Integer nocde);

    @PUT("livraisons/{nocde}")
    Call<Livraison> updateLivraison(
            @Header("Authorization") String token,
            @Path("nocde") Integer nocde,
            @Body Map<String, String> body);

    // --- Dashboard ---
    @GET("dashboard")
    Call<DashboardStats> getDashboard(
            @Header("Authorization") String token,
            @Query("debut") String debut,
            @Query("fin") String fin);

    // --- Livreurs ---
    @GET("livreurs")
    Call<List<Livreur>> getLivreurs(@Header("Authorization") String token);

    // --- Messages ---
    @POST("messages")
    Call<Message> envoyerMessage(
            @Header("Authorization") String token,
            @Body Map<String, Object> body);

    @GET("messages/recus")
    Call<List<Message>> getMessagesRecus(@Header("Authorization") String token);

    @GET("messages/envoyes")
    Call<List<Message>> getMessagesEnvoyes(@Header("Authorization") String token);

    @GET("messages/urgence")
    Call<List<Message>> getMessagesUrgence(@Header("Authorization") String token);

    @PUT("messages/{id}/lu")
    Call<Message> marquerLu(
            @Header("Authorization") String token,
            @Path("id") Integer id);

    @GET("messages/non-lus/count")
    Call<Map<String, Long>> countNonLus(@Header("Authorization") String token);
}
