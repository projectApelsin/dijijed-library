package com.dreamscometrue.libraryvariant.service;

import com.dreamscometrue.libraryvariant.model.Book;
import com.dreamscometrue.libraryvariant.model.repository.AuthorRepository;
import com.dreamscometrue.libraryvariant.model.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    public Book createBook(Book book) {
        if (authorRepository.existsById(book.getAuthor().getId())) {
            return bookRepository.save(book);
        }
        return null;
    }

    public Book updateBook(Long id, Book updatedBook) {
        if (bookRepository.existsById(id)) {
            updatedBook.setId(id);
            return bookRepository.save(updatedBook);
        }
        return null;
    }
}
