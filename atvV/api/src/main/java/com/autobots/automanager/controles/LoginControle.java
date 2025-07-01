package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.jwt.ProvedorJwt;

@RestController
public class LoginControle {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credencial credencial) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credencial.getNomeUsuario(), credencial.getSenha())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = provedorJwt.proverJwt(userDetails.getUsername());

            return ResponseEntity.ok().header("Authorization", "Bearer " + token).body("Login OK");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }
}

