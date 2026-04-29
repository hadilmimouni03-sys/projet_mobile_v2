package com.livraison.repository;

import com.livraison.model.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Integer> {
    Optional<Personnel> findByLogin(String login);
    List<Personnel> findByPosteCodeposte(String codeposte);
}
