package com.livraison.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class LivraisonDTO {
    private Integer nocde;
    private LocalDate dateliv;
    private LocalDate datecde;
    private String etatliv;
    private String modepay;
    private String remarques;

    // Client
    private Integer noclt;
    private String nomClient;
    private String prenomClient;
    private String telClient;
    private String adresseClient;
    private String villeClient;
    private String codePostalClient;
    private String emailClient;

    // Livreur
    private Integer idLivreur;
    private String nomLivreur;
    private String prenomLivreur;

    // Commande
    private Integer ordrelivraison;
    private BigDecimal montantTotal;
    private Integer nombreArticles;

    // Lignes de commande
    private List<LigneDTO> lignes;

    @Data
    public static class LigneDTO {
        private String refarticle;
        private String designation;
        private Integer qtecde;
        private BigDecimal prixV;
        private BigDecimal sousTotal;
    }
}
