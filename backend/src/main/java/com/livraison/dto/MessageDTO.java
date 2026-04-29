package com.livraison.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Integer id;
    private Integer expediteurId;
    private String nomExpediteur;
    private Integer destinataireId;
    private String nomDestinataire;
    private Integer nocde;
    private String contenu;
    private LocalDateTime dateenvoi;
    private Boolean lu;
    private String typeMessage;
}
