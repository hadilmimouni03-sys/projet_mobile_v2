package com.livraison.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class SendMessageRequest {
    private Integer destinataireId;
    private Integer nocde;
    @NotBlank
    private String contenu;
    private String typeMessage;
}
