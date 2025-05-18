package com.dreamscometrue.libraryvariant.service;

import com.dreamscometrue.libraryvariant.model.Book;
import com.dreamscometrue.libraryvariant.model.Borrowing;
import com.dreamscometrue.libraryvariant.model.repository.BookRepository;
import com.dreamscometrue.libraryvariant.model.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Borrowing> getActiveBorrowings() {
        return borrowingRepository.findByReturnedFalse();
    }
    public Borrowing createBorrowing(Borrowing borrowing) {
        Optional<Book> optionalBook = bookRepository.findById(borrowing.getBook().getId());

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            if (book.getAvailable()) {
                borrowing.setBook(book);
                borrowing.setBorrowedDate(new Timestamp(System.currentTimeMillis()));
                book.setAvailable(false);
                bookRepository.save(book);

                return borrowingRepository.save(borrowing);
            }
        }

        return null;
    }

    public Borrowing returnBook(Long id) {
        Optional<Borrowing> borrowing = borrowingRepository.findById(id);
        if (borrowing.isPresent() && !borrowing.get().getReturned()) {
            Borrowing returnedBorrowing = borrowing.get();
            returnedBorrowing.setReturned(true);
            Book book = borrowing.get().getBook();
            book.setAvailable(true);
            bookRepository.save(book);
            return borrowingRepository.save(returnedBorrowing);
        }
        return null;
    }
}
