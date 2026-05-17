package com.example.sge.controller;

import com.example.sge.dto.AuthResponse;
import com.example.sge.dto.LoginRequest;
import com.example.sge.model.Role;
import com.example.sge.model.Utilisateur;
import com.example.sge.repository.UtilisateurRepository;
import com.example.sge.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepo;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authManager;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody LoginRequest req) {
        if (utilisateurRepo.findByUsername(
                req.getUsername()).isPresent())
            return ResponseEntity.badRequest()
                    .body("Username deja utilise");
        Utilisateur u = new Utilisateur();
        u.setUsername(req.getUsername());
        u.setPassword(
                passwordEncoder.encode(req.getPassword()));
        u.setRole(Role.USER);
        utilisateurRepo.save(u);
        return ResponseEntity.status(201).body(
                new AuthResponse(
                        jwtService.generateToken(u),
                        u.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(), req.getPassword()));
        Utilisateur u = utilisateurRepo
                .findByUsername(req.getUsername()).orElseThrow();
        return ResponseEntity.ok(
                new AuthResponse(
                        jwtService.generateToken(u),
                        u.getRole().name()));
    }

    @PostMapping("/login-web")
    public ResponseEntity<Void> loginWeb(
            @RequestParam String username,
            @RequestParam String password) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        Utilisateur u = utilisateurRepo.findByUsername(username).orElseThrow();
        String token = jwtService.generateToken(u);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .build();

        return ResponseEntity.status(302)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .location(URI.create("/bulletins"))
                .build();
    }

    @PostMapping("/logout-web")
    public ResponseEntity<Void> logoutWeb() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();
        return ResponseEntity.status(302)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .location(URI.create("/login"))
                .build();
    }
}
