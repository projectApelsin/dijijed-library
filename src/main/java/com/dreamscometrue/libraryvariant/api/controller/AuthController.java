package com.dreamscometrue.libraryvariant.api.controller;

import com.dreamscometrue.libraryvariant.api.security.JwtService;
import com.dreamscometrue.libraryvariant.dto.AuthenticationRequest;
import com.dreamscometrue.libraryvariant.dto.AuthenticationResponse;
import com.dreamscometrue.libraryvariant.dto.UserDTO;
import com.dreamscometrue.libraryvariant.model.UserClient;
import com.dreamscometrue.libraryvariant.model.repository.UserClientRepository;
import com.dreamscometrue.libraryvariant.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserClientRepository userClientRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(
            UserService userService,
            UserClientRepository userClientRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.userClientRepository = userClientRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        System.out.println(username);
        System.out.println(password);
        userService.register(username, password);
        return new ModelAndView("login"); // перенаправление после успешной регистрации
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(Authentication authentication) {
        String jwt = jwtService.generateAccessToken(authentication.getName());
        return ResponseEntity.ok(Map.of("token", jwt));
    }
    @PostMapping("/login")
    public ModelAndView loginWithForm(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException ex) {
            return new ModelAndView("login", Map.of("error", "Invalid credentials"));
        }

        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 15); // 15 минут

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7 дней

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return new ModelAndView("redirect:/home");
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshUser(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
        String username = jwtService.extractUsername(refreshToken);

        // Генерируем новый access токен
        String newAccessToken = jwtService.generateAccessToken(username);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        return new ModelAndView("register");
    }
}