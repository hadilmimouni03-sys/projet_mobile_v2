package com.livraison.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigCdeId implements Serializable {
    private Integer nocde;
    private String refarticle;
}
