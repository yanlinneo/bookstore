package com.neoyanlin.bookstore.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.neoyanlin.bookstore.entity.Author;

public interface AuthorService {
	public Author save(Author author);
	public List<Author> saveAll(List<Author> authors);
	public Author findById(int id);
	public Optional<Author> findByNameAndDateOfBirth(String name, LocalDate dateOfBirth);
}
