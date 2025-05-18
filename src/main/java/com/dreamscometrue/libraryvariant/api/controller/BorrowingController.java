package com.dreamscometrue.libraryvariant.api.controller;


import com.dreamscometrue.libraryvariant.model.Borrowing;
import com.dreamscometrue.libraryvariant.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @GetMapping("/active")
    public ResponseEntity<List<Borrowing>> getActiveBorrowings() {
        List<Borrowing> borrowings = borrowingService.getActiveBorrowings();
        return ResponseEntity.ok(borrowings);
    }

    @PostMapping("")
    public ResponseEntity<Borrowing> createBorrowing(@RequestBody Borrowing borrowing) {
        Borrowing createdBorrowing = borrowingService.createBorrowing(borrowing);
        return createdBorrowing != null ? ResponseEntity.ok(createdBorrowing) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Borrowing> returnBook(@PathVariable Long id) {
        Borrowing returnedBorrowing = borrowingService.returnBook(id);
        return returnedBorrowing != null ? ResponseEntity.ok(returnedBorrowing) : ResponseEntity.notFound().build();
    }
}
