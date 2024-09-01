package com.neoyanlin.bookstore.dao;

import java.util.List;
import java.util.Optional;

import com.neoyanlin.bookstore.entity.Book;

public interface BookRepository {
	public Book save(Book book);
	public List<Book> findAll();
	public Book findById(int id);
	public Optional<Book> findByISBN(String isbn);
	public void deleteById(int id);
	public List<Book> findByTitle(String title);
	public List<Book> findByTitleAndAuthorName(String title, String authorName);
	public List<Book> findByAuthorName(String authorName);
}
