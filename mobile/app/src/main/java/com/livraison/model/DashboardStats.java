package com.livraison.model;

import java.util.List;
import java.util.Map;

public class DashboardStats {
    public int totalLivraisons;
    public int livre;
    public int nonLivre;
    public int enCours;
    public int enAttente;
    public List<StatLivreur> parLivreur;
    public List<StatClient> parClient;

    public static class StatLivreur {
        public Integer idLivreur;
        public String nomLivreur;
        public Map<String, Double> parEtat;
        public long total;
    }

    public static class StatClient {
        public Integer noclt;
        public String nomClient;
        public Map<String, Double> parEtat;
        public long total;
    }
}
