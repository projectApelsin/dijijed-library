package com.dreamscometrue.libraryvariant.model.repository;

import com.dreamscometrue.libraryvariant.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}