package com.neoyanlin.bookstore.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neoyanlin.bookstore.dao.AuthorRepository;
import com.neoyanlin.bookstore.entity.Author;

import jakarta.transaction.Transactional;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	private AuthorRepository authorRepository;
	
	@Autowired
	public AuthorServiceImpl(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}
	
	@Override
	@Transactional
	public Author save(Author author) {
		return authorRepository.save(author);
	}
	
	@Override
	@Transactional
	public List<Author> saveAll(List<Author> authors) {
		for (int i = 0; i < authors.size(); i++) {

			Optional<Author> dbAuthor = findByNameAndDateOfBirth(authors.get(i).getName(), authors.get(i).getDateOfBirth());
			
			if (dbAuthor.isPresent()) {
				authors.set(i, dbAuthor.get());
			} else {
				authors.set(i, save(authors.get(i)));
			}
		}
		
		return authors;
	}

	@Override
	public Author findById(int id) {
		return authorRepository.findById(id);
	}

	@Override
	public Optional<Author> findByNameAndDateOfBirth(String name, LocalDate dateOfBirth) {
		return authorRepository.findByNameAndDateOfBirth(name, dateOfBirth);
	}

}
