package com.livraison.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateLivraisonRequest {
    @NotBlank
    private String etatliv;
    private String remarques;
    private String modepay;
}
