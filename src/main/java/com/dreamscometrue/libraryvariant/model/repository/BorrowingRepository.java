package com.dreamscometrue.libraryvariant.model.repository;

import com.dreamscometrue.libraryvariant.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findByReturnedFalse();
}