package com.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer idpers;
    private String nom;
    private String prenom;
    private String role;
    private String login;
}
