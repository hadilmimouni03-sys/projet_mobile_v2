package com.livraison.service;

import com.livraison.dto.DashboardDTO;
import com.livraison.model.LivraisonCom;
import com.livraison.repository.LivraisonComRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired private LivraisonComRepository livraisonRepo;

    public DashboardDTO getDashboard(LocalDate debut, LocalDate fin) {
        if (debut == null) debut = LocalDate.now().withDayOfMonth(1);
        if (fin == null) fin = LocalDate.now();

        List<LivraisonCom> livraisons = livraisonRepo.findForDashboard(debut, fin);

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalLivraisons(livraisons.size());
        dto.setLivre((int) livraisons.stream().filter(l -> "LIVRE".equals(l.getEtatliv())).count());
        dto.setNonLivre((int) livraisons.stream().filter(l -> "NON_LIVRE".equals(l.getEtatliv())).count());
        dto.setEnCours((int) livraisons.stream().filter(l -> "EN_COURS".equals(l.getEtatliv())).count());
        dto.setEnAttente((int) livraisons.stream().filter(l -> "EN_ATTENTE".equals(l.getEtatliv())).count());

        // Stats par livreur
        Map<Integer, List<LivraisonCom>> parLivreurMap = livraisons.stream()
                .filter(l -> l.getLivreur() != null)
                .collect(Collectors.groupingBy(l -> l.getLivreur().getIdpers()));

        List<DashboardDTO.StatLivreur> statsLivreur = new ArrayList<>();
        parLivreurMap.forEach((idLivreur, liste) -> {
            DashboardDTO.StatLivreur stat = new DashboardDTO.StatLivreur();
            stat.setIdLivreur(idLivreur);
            stat.setNomLivreur(liste.get(0).getLivreur().getNompers()
                    + " " + liste.get(0).getLivreur().getPrenompers());
            Map<String, Long> parEtat = liste.stream()
                    .collect(Collectors.groupingBy(LivraisonCom::getEtatliv, Collectors.counting()));
            stat.setParEtat(parEtat);
            stat.setTotal(liste.size());
            statsLivreur.add(stat);
        });
        dto.setParLivreur(statsLivreur);

        // Stats par client
        Map<Integer, List<LivraisonCom>> parClientMap = livraisons.stream()
                .filter(l -> l.getCommande() != null && l.getCommande().getClient() != null)
                .collect(Collectors.groupingBy(l -> l.getCommande().getClient().getNoclt()));

        List<DashboardDTO.StatClient> statsClient = new ArrayList<>();
        parClientMap.forEach((noclt, liste) -> {
            DashboardDTO.StatClient stat = new DashboardDTO.StatClient();
            stat.setNoclt(noclt);
            stat.setNomClient(liste.get(0).getCommande().getClient().getNomclt()
                    + " " + liste.get(0).getCommande().getClient().getPrenomclt());
            Map<String, Long> parEtat = liste.stream()
                    .collect(Collectors.groupingBy(LivraisonCom::getEtatliv, Collectors.counting()));
            stat.setParEtat(parEtat);
            stat.setTotal(liste.size());
            statsClient.add(stat);
        });
        dto.setParClient(statsClient);

        return dto;
    }
}
