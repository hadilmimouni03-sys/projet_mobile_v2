package com.livraison.repository;

import com.livraison.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // Messages reçus par un utilisateur (non lus en premier)
    List<Message> findByDestinataireIdpersOrderByDateenvoiDesc(Integer idpers);

    // Messages envoyés par un utilisateur
    List<Message> findByExpediteurIdpersOrderByDateenvoiDesc(Integer idpers);

    // Messages non lus d'un utilisateur
    List<Message> findByDestinataireIdpersAndLuFalse(Integer idpers);

    // Messages concernant une commande
    List<Message> findByNocdeOrderByDateenvoiDesc(Integer nocde);

    // Tous les messages d'urgence pour le contrôleur
    List<Message> findByTypeMessageOrderByDateenvoiDesc(String typeMessage);
}
