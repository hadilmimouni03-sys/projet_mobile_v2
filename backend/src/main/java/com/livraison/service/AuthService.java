package com.livraison.service;

import com.livraison.dto.LoginRequest;
import com.livraison.dto.LoginResponse;
import com.livraison.model.Personnel;
import com.livraison.repository.PersonnelRepository;
import com.livraison.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired private PersonnelRepository personnelRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        Personnel p = personnelRepo.findByLogin(request.getLogin())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Identifiants incorrects"));

        if (!passwordEncoder.matches(request.getMotP(), p.getMotP())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants incorrects");
        }

        String role = p.getPoste().getCodeposte();
        String token = jwtUtil.generateToken(p.getLogin(), role, p.getIdpers());

        return new LoginResponse(
                token,
                p.getIdpers(),
                p.getNompers(),
                p.getPrenompers(),
                role,
                p.getLogin()
        );
    }
}
