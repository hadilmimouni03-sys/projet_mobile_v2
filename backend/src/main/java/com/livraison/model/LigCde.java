package com.livraison.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "LigCdes")
@IdClass(LigCdeId.class)
public class LigCde {

    @Id
    @Column(name = "nocde")
    private Integer nocde;

    @Id
    @Column(name = "refarticle", length = 20)
    private String refarticle;

    @Column(name = "qtecde", nullable = false)
    private Integer qtecde;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nocde", insertable = false, updatable = false)
    private Commande commande;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "refarticle", insertable = false, updatable = false)
    private Article article;
}
