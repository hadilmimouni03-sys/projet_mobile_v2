package com.livraison.repository;

import com.livraison.model.LivraisonCom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LivraisonComRepository extends JpaRepository<LivraisonCom, Integer> {

    // Livraisons par livreur pour une date
    List<LivraisonCom> findByLivreurIdpersAndDateliv(Integer idpers, LocalDate dateliv);

    // Livraisons d'une période
    @Query("SELECT l FROM LivraisonCom l WHERE l.dateliv BETWEEN :debut AND :fin")
    List<LivraisonCom> findByPeriode(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    // Livraisons du jour
    List<LivraisonCom> findByDateliv(LocalDate dateliv);

    // Recherche multicritère
    @Query("""
        SELECT l FROM LivraisonCom l
        WHERE (:dateliv IS NULL OR l.dateliv = :dateliv)
          AND (:livreurId IS NULL OR l.livreur.idpers = :livreurId)
          AND (:etatliv IS NULL OR l.etatliv = :etatliv)
          AND (:noclt IS NULL OR l.commande.client.noclt = :noclt)
          AND (:nocde IS NULL OR l.nocde = :nocde)
    """)
    List<LivraisonCom> rechercher(
            @Param("dateliv")   LocalDate dateliv,
            @Param("livreurId") Integer livreurId,
            @Param("etatliv")   String etatliv,
            @Param("noclt")     Integer noclt,
            @Param("nocde")     Integer nocde
    );

    // Livraisons période pour dashboard
    @Query("SELECT l FROM LivraisonCom l WHERE l.dateliv BETWEEN :debut AND :fin")
    List<LivraisonCom> findForDashboard(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}
