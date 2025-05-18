package com.dreamscometrue.libraryvariant.model.repository;

import com.dreamscometrue.libraryvariant.model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClientRepository extends JpaRepository<UserClient, Long> {
    UserClient findByUsername(String username);
}