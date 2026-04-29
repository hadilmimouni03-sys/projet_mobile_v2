package com.livraison.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expediteur", nullable = false)
    private Personnel expediteur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinataire")
    private Personnel destinataire;

    @Column(name = "nocde")
    private Integer nocde;

    @Column(name = "contenu", columnDefinition = "TEXT", nullable = false)
    private String contenu;

    @Column(name = "dateenvoi")
    private LocalDateTime dateenvoi;

    @Column(name = "lu")
    private Boolean lu = false;

    @Column(name = "type_message", length = 20)
    private String typeMessage;

    @PrePersist
    public void prePersist() {
        if (dateenvoi == null) dateenvoi = LocalDateTime.now();
        if (lu == null) lu = false;
        if (typeMessage == null) typeMessage = "INFO";
    }
}
