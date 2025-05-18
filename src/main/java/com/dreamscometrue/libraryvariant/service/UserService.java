package com.dreamscometrue.libraryvariant.service;

import com.dreamscometrue.libraryvariant.model.UserClient;
import com.dreamscometrue.libraryvariant.model.repository.UserClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserClientRepository userClientRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    public UserService(UserClientRepository userClientRepository, PasswordEncoder passwordEncoder) {
        this.userClientRepository = userClientRepository;

        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password) {
        UserClient userClient = new UserClient();
        userClient.setUsername(username);
        userClient.setPassword(passwordEncoder.encode(password));
        userClientRepository.save(userClient);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserClient user = userClientRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }


}
