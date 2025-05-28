package com.dreamscometrue.libraryvariant.api.security;

import com.dreamscometrue.libraryvariant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthenticationFilter;
    @Autowired
    private EncoderConfig encoderConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        // Вимикаємо CSRF для API endpoints
                        .ignoringRequestMatchers("/api/**", "/auth/**")
                )
                .authorizeHttpRequests(auth -> auth
                        // Публічні endpoints
                        .requestMatchers("/auth/**", "/api/auth/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        // API endpoints - використовують JWT
                        .requestMatchers("/api/**").authenticated()
                        // Веб-сторінки - використовують сесії
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // Для API - stateless сесії (JWT)
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        // Максимальна кількість сесій для користувача
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .formLogin(form -> form
                        // Налаштування для веб-форм
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        // Налаштування виходу для веб-сторінок
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                // JWT фільтр тільки для API endpoints
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(encoderConfig.passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }
}