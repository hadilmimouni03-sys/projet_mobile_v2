package com.livraison.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "Clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noclt")
    private Integer noclt;

    @Column(name = "nomclt", nullable = false, length = 50)
    private String nomclt;

    @Column(name = "prenomclt", length = 50)
    private String prenomclt;

    @Column(name = "adrclt")
    private String adrclt;

    @Column(name = "villeclt", length = 100)
    private String villeclt;

    @Column(name = "code_postal", length = 10)
    private String codePostal;

    @Column(name = "telclt", length = 20)
    private String telclt;

    @Column(name = "adrmail", length = 150)
    private String adrmail;
}
