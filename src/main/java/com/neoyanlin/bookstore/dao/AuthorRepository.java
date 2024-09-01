package com.neoyanlin.bookstore.dao;

import java.time.LocalDate;
import java.util.Optional;

import com.neoyanlin.bookstore.entity.Author;

public interface AuthorRepository {
	public Author save(Author author);
	public Author findById(int id);
	public Optional<Author> findByNameAndDateOfBirth(String name, LocalDate dateOfBirth);
}
