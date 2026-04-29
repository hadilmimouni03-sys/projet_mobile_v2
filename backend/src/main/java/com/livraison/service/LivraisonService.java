package com.livraison.service;

import com.livraison.dto.LivraisonDTO;
import com.livraison.dto.UpdateLivraisonRequest;
import com.livraison.model.LigCde;
import com.livraison.model.LivraisonCom;
import com.livraison.repository.LigCdeRepository;
import com.livraison.repository.LivraisonComRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivraisonService {

    @Autowired private LivraisonComRepository livraisonRepo;
    @Autowired private LigCdeRepository ligCdeRepo;

    public List<LivraisonDTO> getLivraisonsAujourdhui() {
        return livraisonRepo.findByDateliv(LocalDate.now())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<LivraisonDTO> getLivraisonsDuLivreur(Integer idLivreur) {
        return livraisonRepo.findByLivreurIdpersAndDateliv(idLivreur, LocalDate.now())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<LivraisonDTO> rechercher(LocalDate dateliv, Integer livreurId,
                                          String etatliv, Integer noclt, Integer nocde) {
        return livraisonRepo.rechercher(dateliv, livreurId, etatliv, noclt, nocde)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<LivraisonDTO> getLivraisonsParPeriode(LocalDate debut, LocalDate fin) {
        return livraisonRepo.findByPeriode(debut, fin)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public LivraisonDTO getDetail(Integer nocde) {
        LivraisonCom liv = livraisonRepo.findById(nocde)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livraison introuvable"));
        return toDetailDTO(liv);
    }

    public LivraisonDTO updateLivraison(Integer nocde, UpdateLivraisonRequest req) {
        LivraisonCom liv = livraisonRepo.findById(nocde)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livraison introuvable"));
        liv.setEtatliv(req.getEtatliv());
        if (req.getRemarques() != null) liv.setRemarques(req.getRemarques());
        if (req.getModepay() != null) liv.setModepay(req.getModepay());
        return toDetailDTO(livraisonRepo.save(liv));
    }

    // --- Mapping ---

    private LivraisonDTO toDTO(LivraisonCom l) {
        LivraisonDTO dto = new LivraisonDTO();
        dto.setNocde(l.getNocde());
        dto.setDateliv(l.getDateliv());
        dto.setEtatliv(l.getEtatliv());
        dto.setModepay(l.getModepay());
        dto.setRemarques(l.getRemarques());
        if (l.getCommande() != null) {
            dto.setDatecde(l.getCommande().getDatecde());
            if (l.getCommande().getClient() != null) {
                dto.setNoclt(l.getCommande().getClient().getNoclt());
                dto.setNomClient(l.getCommande().getClient().getNomclt());
                dto.setPrenomClient(l.getCommande().getClient().getPrenomclt());
                dto.setTelClient(l.getCommande().getClient().getTelclt());
                dto.setVilleClient(l.getCommande().getClient().getVilleclt());
                dto.setAdresseClient(l.getCommande().getClient().getAdrclt());
                dto.setCodePostalClient(l.getCommande().getClient().getCodePostal());
                dto.setEmailClient(l.getCommande().getClient().getAdrmail());
            }
        }
        if (l.getLivreur() != null) {
            dto.setIdLivreur(l.getLivreur().getIdpers());
            dto.setNomLivreur(l.getLivreur().getNompers());
            dto.setPrenomLivreur(l.getLivreur().getPrenompers());
        }
        return dto;
    }

    private LivraisonDTO toDetailDTO(LivraisonCom l) {
        LivraisonDTO dto = toDTO(l);
        List<LigCde> lignes = ligCdeRepo.findByNocde(l.getNocde());
        BigDecimal montant = BigDecimal.ZERO;
        int nbArticles = 0;
        List<LivraisonDTO.LigneDTO> lignesDTO = new ArrayList<>();
        for (LigCde lc : lignes) {
            LivraisonDTO.LigneDTO ldto = new LivraisonDTO.LigneDTO();
            ldto.setRefarticle(lc.getRefarticle());
            ldto.setQtecde(lc.getQtecde());
            if (lc.getArticle() != null) {
                ldto.setDesignation(lc.getArticle().getDesignation());
                ldto.setPrixV(lc.getArticle().getPrixV());
                BigDecimal sous = lc.getArticle().getPrixV()
                        .multiply(BigDecimal.valueOf(lc.getQtecde()));
                ldto.setSousTotal(sous);
                montant = montant.add(sous);
            }
            nbArticles += lc.getQtecde();
            lignesDTO.add(ldto);
        }
        dto.setMontantTotal(montant);
        dto.setNombreArticles(nbArticles);
        dto.setLignes(lignesDTO);
        return dto;
    }
}
