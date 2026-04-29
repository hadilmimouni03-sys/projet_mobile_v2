package com.livraison.controller;

import com.livraison.dto.MessageDTO;
import com.livraison.dto.SendMessageRequest;
import com.livraison.security.JwtUtil;
import com.livraison.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired private MessageService messageService;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<MessageDTO> envoyer(
            @Valid @RequestBody SendMessageRequest request,
            HttpServletRequest req) {
        Integer expediteurId = jwtUtil.extractIdpers(extractToken(req));
        return ResponseEntity.ok(messageService.envoyer(expediteurId, request));
    }

    @GetMapping("/recus")
    public ResponseEntity<List<MessageDTO>> getRecus(HttpServletRequest req) {
        Integer idpers = jwtUtil.extractIdpers(extractToken(req));
        return ResponseEntity.ok(messageService.getMessagesRecus(idpers));
    }

    @GetMapping("/envoyes")
    public ResponseEntity<List<MessageDTO>> getEnvoyes(HttpServletRequest req) {
        Integer idpers = jwtUtil.extractIdpers(extractToken(req));
        return ResponseEntity.ok(messageService.getMessagesEnvoyes(idpers));
    }

    @GetMapping("/urgence")
    public ResponseEntity<List<MessageDTO>> getUrgence() {
        return ResponseEntity.ok(messageService.getMessagesUrgence());
    }

    @GetMapping("/non-lus/count")
    public ResponseEntity<Map<String, Long>> countNonLus(HttpServletRequest req) {
        Integer idpers = jwtUtil.extractIdpers(extractToken(req));
        return ResponseEntity.ok(Map.of("count", messageService.countNonLus(idpers)));
    }

    @PutMapping("/{id}/lu")
    public ResponseEntity<MessageDTO> marquerLu(@PathVariable Integer id) {
        return ResponseEntity.ok(messageService.marquerLu(id));
    }

    private String extractToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return header != null ? header.substring(7) : "";
    }
}
