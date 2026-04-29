package com.livraison.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardDTO {
    private int totalLivraisons;
    private int livre;
    private int nonLivre;
    private int enCours;
    private int enAttente;
    private List<StatLivreur> parLivreur;
    private List<StatClient> parClient;

    @Data
    public static class StatLivreur {
        private Integer idLivreur;
        private String nomLivreur;
        private Map<String, Long> parEtat;
        private long total;
    }

    @Data
    public static class StatClient {
        private Integer noclt;
        private String nomClient;
        private Map<String, Long> parEtat;
        private long total;
    }
}
