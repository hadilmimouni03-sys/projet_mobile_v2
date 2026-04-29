package com.livraison.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nocde")
    private Integer nocde;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "noclt", nullable = false)
    private Client client;

    @Column(name = "datecde", nullable = false)
    private LocalDate datecde;

    @Column(name = "etatcde", length = 20)
    private String etatcde;

    @OneToMany(mappedBy = "commande", fetch = FetchType.LAZY)
    private List<LigCde> lignes;
}
