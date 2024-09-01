package com.neoyanlin.bookstore.dao;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.neoyanlin.bookstore.entity.Author;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
	private EntityManager entityManager;
	
	@Autowired
	public AuthorRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Author save(Author author) {
		Author dbAuthor = entityManager.merge(author);
		return dbAuthor;
	}
	
	@Override
	public Author findById(int id) {
		Author dbAuthor = entityManager.find(Author.class, id);
		return dbAuthor;
	}

	@Override
	public Optional<Author> findByNameAndDateOfBirth(String name, LocalDate dateOfBirth) {
		TypedQuery<Author> query = entityManager.createQuery("from Author WHERE name = :name AND dateOfBirth = :dateOfBirth", Author.class);
		query.setParameter("name", name);
		query.setParameter("dateOfBirth", dateOfBirth);
		
		try {
			Author author = query.getSingleResult();
			return Optional.of(author);
		} catch (NoResultException ex) {
			return Optional.empty();
		}
	}

}
