package com.livraison.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "LivraisonCom")
public class LivraisonCom {

    @Id
    @Column(name = "nocde")
    private Integer nocde;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "nocde")
    private Commande commande;

    @Column(name = "dateliv")
    private LocalDate dateliv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livreur")
    private Personnel livreur;

    @Column(name = "modepay", length = 20)
    private String modepay;

    @Column(name = "etatliv", nullable = false, length = 30)
    private String etatliv;

    @Column(name = "remarques", columnDefinition = "TEXT")
    private String remarques;
}
