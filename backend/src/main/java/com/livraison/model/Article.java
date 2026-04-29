package com.livraison.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Articles")
public class Article {

    @Id
    @Column(name = "refarticle", length = 20)
    private String refarticle;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "prixA", precision = 10, scale = 2)
    private BigDecimal prixA;

    @Column(name = "prixV", precision = 10, scale = 2)
    private BigDecimal prixV;

    @Column(name = "codetva", length = 10)
    private String codetva;

    @Column(name = "categorie", length = 100)
    private String categorie;

    @Column(name = "qtestock")
    private Integer qtestock;
}
