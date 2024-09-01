package com.neoyanlin.bookstore.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import com.neoyanlin.bookstore.entity.Book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class BookRepositoryImpl implements BookRepository {
	
	private EntityManager entityManager;
	
	@Autowired
	public BookRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Book save(Book book) {
		Book dbBook = entityManager.merge(book);
		return dbBook;
	}

	@Override
	public Book findById(int id) {
		Book dbBook = entityManager.find(Book.class, id);
		return dbBook;
	}
	
	@Override
	public List<Book> findAll() {
		TypedQuery<Book> query = entityManager.createQuery("from Book", Book.class);
		List<Book> dbBooks = query.getResultList();
		return dbBooks;
	}

	@Override
	public Optional<Book> findByISBN(String isbn) {
		TypedQuery<Book> query = entityManager.createQuery("FROM Book WHERE isbn = :isbn", Book.class);
		query.setParameter("isbn", isbn);
		
		try {
			Book book = query.getSingleResult();
			return Optional.of(book);
		} catch (NoResultException ex) {
			return Optional.empty();
		}
	}


	@Override
	public void deleteById(int id) {
		Book book = entityManager.find(Book.class, id);
		entityManager.remove(book);
		
	}

	@Override
	public List<Book> findByTitle(String title) {
		TypedQuery<Book> query = entityManager.createQuery("FROM Book WHERE title LIKE :title", Book.class);
		query.setParameter("title", "%" + title + "%");
		
		List<Book> books = query.getResultList();
		return books;
	}

	@Override
	public List<Book> findByTitleAndAuthorName(String title, String authorName) {
		TypedQuery<Book> query = entityManager.createQuery("FROM Book B join B.authors A WHERE A.name = :name AND B.title LIKE :title", Book.class);
		query.setParameter("title", "%" + title + "%");
		query.setParameter("name", authorName);
		
		List<Book> books = query.getResultList();
		return books;
	}


	@Override
	public List<Book> findByAuthorName(String authorName) {
		TypedQuery<Book> query = entityManager.createQuery("FROM Book B join B.authors A WHERE A.name = :name", Book.class);
		query.setParameter("name", authorName);
		
		List<Book> books = query.getResultList();
		return books;
	}

}
