package com.dreamscometrue.libraryvariant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "available", nullable = false)
    private Boolean available = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
    @JsonIgnore
    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private Set<Borrowing> borrowings = new LinkedHashSet<>();

}