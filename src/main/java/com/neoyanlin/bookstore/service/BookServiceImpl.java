package com.neoyanlin.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neoyanlin.bookstore.dao.BookRepository;
import com.neoyanlin.bookstore.entity.Book;

import jakarta.transaction.Transactional;

@Service
public class BookServiceImpl implements BookService {
	
	private BookRepository bookRepository;
	
	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	@Transactional
	public Book save(Book book) {
		return bookRepository.save(book);
	}
	
	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book findById(int id) {
		return bookRepository.findById(id);
	}

	@Override
	public Book findByISBN(String isbn) {
		Optional<Book> dbBook = bookRepository.findByISBN(isbn);
		
		if (dbBook.isPresent()) {
			return dbBook.get();
		}
		
		return null;
	}
	
	@Override
	@Transactional
	public void deleteById(int id) {
		bookRepository.deleteById(id);
	}

	@Override
	public List<Book> findByTitleAndAuthorName(String title, String authorName) {
		return bookRepository.findByTitleAndAuthorName(title, authorName);
		
	}

	@Override
	public List<Book> findByTitle(String title) {
		return bookRepository.findByTitle(title);
	}

	@Override
	public List<Book> findByAuthorName(String authorName) {
		return bookRepository.findByAuthorName(authorName);
	}

}
