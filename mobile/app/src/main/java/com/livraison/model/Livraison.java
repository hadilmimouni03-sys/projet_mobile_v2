package com.livraison.model;

import java.util.List;

public class Livraison {
    public Integer nocde;
    public String dateliv;
    public String datecde;
    public String etatliv;
    public String modepay;
    public String remarques;

    // Client
    public Integer noclt;
    public String nomClient;
    public String prenomClient;
    public String telClient;
    public String adresseClient;
    public String villeClient;
    public String codePostalClient;
    public String emailClient;

    // Livreur
    public Integer idLivreur;
    public String nomLivreur;
    public String prenomLivreur;

    // Montants
    public Double montantTotal;
    public Integer nombreArticles;

    // Lignes
    public List<Ligne> lignes;

    public static class Ligne {
        public String refarticle;
        public String designation;
        public Integer qtecde;
        public Double prixV;
        public Double sousTotal;
    }

    public String getEtatLabel() {
        if (etatliv == null) return "Inconnu";
        switch (etatliv) {
            case "LIVRE":      return "Livré";
            case "NON_LIVRE":  return "Non livré";
            case "EN_COURS":   return "En cours";
            case "EN_ATTENTE": return "En attente";
            case "REPORTE":    return "Reporté";
            default:           return etatliv;
        }
    }

    public String getNomCompletClient() {
        return (nomClient != null ? nomClient : "") + " " + (prenomClient != null ? prenomClient : "");
    }
}
