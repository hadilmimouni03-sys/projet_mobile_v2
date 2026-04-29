package com.livraison.service;

import com.livraison.dto.MessageDTO;
import com.livraison.dto.SendMessageRequest;
import com.livraison.model.Message;
import com.livraison.model.Personnel;
import com.livraison.repository.MessageRepository;
import com.livraison.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired private MessageRepository messageRepo;
    @Autowired private PersonnelRepository personnelRepo;

    public MessageDTO envoyer(Integer expediteurId, SendMessageRequest req) {
        Personnel exp = personnelRepo.findById(expediteurId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expéditeur introuvable"));

        Message msg = new Message();
        msg.setExpediteur(exp);
        msg.setContenu(req.getContenu());
        msg.setNocde(req.getNocde());
        msg.setTypeMessage(req.getTypeMessage() != null ? req.getTypeMessage() : "INFO");

        if (req.getDestinataireId() != null) {
            Personnel dest = personnelRepo.findById(req.getDestinataireId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destinataire introuvable"));
            msg.setDestinataire(dest);
        }

        return toDTO(messageRepo.save(msg));
    }

    public List<MessageDTO> getMessagesRecus(Integer idpers) {
        return messageRepo.findByDestinataireIdpersOrderByDateenvoiDesc(idpers)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getMessagesEnvoyes(Integer idpers) {
        return messageRepo.findByExpediteurIdpersOrderByDateenvoiDesc(idpers)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> getMessagesUrgence() {
        return messageRepo.findByTypeMessageOrderByDateenvoiDesc("URGENCE")
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MessageDTO marquerLu(Integer messageId) {
        Message msg = messageRepo.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message introuvable"));
        msg.setLu(true);
        return toDTO(messageRepo.save(msg));
    }

    public long countNonLus(Integer idpers) {
        return messageRepo.findByDestinataireIdpersAndLuFalse(idpers).size();
    }

    private MessageDTO toDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setContenu(m.getContenu());
        dto.setDateenvoi(m.getDateenvoi());
        dto.setLu(m.getLu());
        dto.setTypeMessage(m.getTypeMessage());
        dto.setNocde(m.getNocde());
        if (m.getExpediteur() != null) {
            dto.setExpediteurId(m.getExpediteur().getIdpers());
            dto.setNomExpediteur(m.getExpediteur().getNompers()
                    + " " + m.getExpediteur().getPrenompers());
        }
        if (m.getDestinataire() != null) {
            dto.setDestinataireId(m.getDestinataire().getIdpers());
            dto.setNomDestinataire(m.getDestinataire().getNompers()
                    + " " + m.getDestinataire().getPrenompers());
        }
        return dto;
    }
}
