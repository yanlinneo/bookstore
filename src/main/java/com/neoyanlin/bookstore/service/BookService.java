package com.neoyanlin.bookstore.service;

import java.util.List;

import com.neoyanlin.bookstore.entity.Book;

public interface BookService {
	public Book save(Book book);
	public List<Book> findAll();
	public Book findById(int id);
	public Book findByISBN(String isbn);
	public void deleteById(int id);
	public List<Book> findByTitleAndAuthorName(String title, String authorName);
	public List<Book> findByTitle(String title);
	public List<Book> findByAuthorName(String authorName);
	
}
