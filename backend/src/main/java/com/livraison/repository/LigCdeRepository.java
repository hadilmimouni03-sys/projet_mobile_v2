package com.livraison.repository;

import com.livraison.model.LigCde;
import com.livraison.model.LigCdeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigCdeRepository extends JpaRepository<LigCde, LigCdeId> {
    List<LigCde> findByNocde(Integer nocde);
}
