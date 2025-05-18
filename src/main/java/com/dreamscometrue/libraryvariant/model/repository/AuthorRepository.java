package com.dreamscometrue.libraryvariant.model.repository;

import com.dreamscometrue.libraryvariant.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}