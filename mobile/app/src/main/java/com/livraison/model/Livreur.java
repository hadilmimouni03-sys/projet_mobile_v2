package com.livraison.model;

public class Livreur {
    public Integer idpers;
    public String nom;
    public String prenom;
    public String tel;

    public String getNomComplet() {
        return nom + " " + prenom;
    }
}
