package com.livraison.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "Postes")
public class Poste {

    @Id
    @Column(name = "codeposte", length = 10)
    private String codeposte;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "indice")
    private Integer indice;
}
