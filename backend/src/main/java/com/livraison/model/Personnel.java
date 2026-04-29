package com.livraison.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Personnel")
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpers")
    private Integer idpers;

    @Column(name = "nompers", nullable = false, length = 50)
    private String nompers;

    @Column(name = "prenompers", nullable = false, length = 50)
    private String prenompers;

    @Column(name = "adrpers")
    private String adrpers;

    @Column(name = "villepers", length = 100)
    private String villepers;

    @Column(name = "telpers", length = 20)
    private String telpers;

    @Column(name = "d_embauche")
    private LocalDate dEmbauche;

    @Column(name = "Login", unique = true, nullable = false, length = 50)
    private String login;

    @JsonIgnore
    @Column(name = "motP", nullable = false)
    private String motP;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codeposte")
    private Poste poste;
}
