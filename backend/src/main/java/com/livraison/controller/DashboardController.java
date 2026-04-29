package com.livraison.controller;

import com.livraison.dto.DashboardDTO;
import com.livraison.model.Personnel;
import com.livraison.repository.PersonnelRepository;
import com.livraison.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired private DashboardService dashboardService;
    @Autowired private PersonnelRepository personnelRepo;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(dashboardService.getDashboard(debut, fin));
    }

    @GetMapping("/livreurs")
    public ResponseEntity<List<PersonnelDTO>> getLivreurs() {
        List<Personnel> livreurs = personnelRepo.findByPosteCodeposte("LIV");
        List<PersonnelDTO> dtos = livreurs.stream().map(p -> {
            PersonnelDTO dto = new PersonnelDTO();
            dto.idpers = p.getIdpers();
            dto.nom = p.getNompers();
            dto.prenom = p.getPrenompers();
            dto.tel = p.getTelpers();
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    static class PersonnelDTO {
        public Integer idpers;
        public String nom;
        public String prenom;
        public String tel;
    }
}
