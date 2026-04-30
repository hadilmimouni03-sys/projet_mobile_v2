package com.livraison.controller;

import com.livraison.dto.LivraisonDTO;
import com.livraison.dto.UpdateLivraisonRequest;
import com.livraison.security.JwtUtil;
import com.livraison.service.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {

    @Autowired private LivraisonService livraisonService;
    @Autowired private JwtUtil jwtUtil;

    // --- Contrôleur : toutes les livraisons du jour ---
    @GetMapping("/today")
    public ResponseEntity<List<LivraisonDTO>> getLivraisonsAujourdhui() {
        return ResponseEntity.ok(livraisonService.getLivraisonsAujourdhui());
    }

    // --- Contrôleur : livraisons sur une période ---
    @GetMapping
    public ResponseEntity<List<LivraisonDTO>> getLivraisons(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        if (debut == null) debut = LocalDate.now().minusMonths(1);
        if (fin == null) fin = LocalDate.now();
        return ResponseEntity.ok(livraisonService.getLivraisonsParPeriode(debut, fin));
    }

    // --- Contrôleur / Livreur : recherche multicritère ---
    @GetMapping("/recherche")
    public ResponseEntity<List<LivraisonDTO>> rechercher(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateliv,
            @RequestParam(required = false) Integer livreurId,
            @RequestParam(required = false) String etatliv,
            @RequestParam(required = false) Integer noclt,
            @RequestParam(required = false) Integer nocde) {
        return ResponseEntity.ok(livraisonService.rechercher(dateliv, livreurId, etatliv, noclt, nocde));
    }

    // --- Livreur : mes livraisons du jour ---
    @GetMapping("/mes-livraisons")
    public ResponseEntity<List<LivraisonDTO>> getMesLivraisons(HttpServletRequest req) {
        String token = extractToken(req);
        Integer idLivreur = jwtUtil.extractIdpers(token);
        return ResponseEntity.ok(livraisonService.getLivraisonsDuLivreur(idLivreur));
    }

    // --- Détail d'une livraison ---
    @GetMapping("/{nocde}")
    public ResponseEntity<LivraisonDTO> getDetail(@PathVariable Integer nocde) {
        return ResponseEntity.ok(livraisonService.getDetail(nocde));
    }

    // --- Livreur : modifier une livraison ---
    @PutMapping("/{nocde}")
    public ResponseEntity<LivraisonDTO> updateLivraison(
            @PathVariable Integer nocde,
            @Valid @RequestBody UpdateLivraisonRequest request) {
        return ResponseEntity.ok(livraisonService.updateLivraison(nocde, request));
    }

    private String extractToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return header != null ? header.substring(7) : "";
    }
}
