package com.neoyanlin.bookstore.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neoyanlin.bookstore.entity.Author;
import com.neoyanlin.bookstore.entity.Book;
import com.neoyanlin.bookstore.exception.BadRequestException;
import com.neoyanlin.bookstore.exception.ApiResponse;
import com.neoyanlin.bookstore.exception.ResourceNotFoundException;
import com.neoyanlin.bookstore.service.AuthorService;
import com.neoyanlin.bookstore.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BookController {
	private BookService bookService;
	private AuthorService authorService;
	
	@Autowired
	public BookController(BookService bookService, AuthorService authorService) {
		this.bookService = bookService;
		this.authorService = authorService;
	}
	
	@GetMapping("/books")
	public List<Book> findBook() {
		List<Book> books = bookService.findAll();
		return books;
	}
	
	@GetMapping("/books/{id}")
	public Book findBookByISBN(@PathVariable int id) {
		Book book = bookService.findById(id);
		
		if (book == null) {
			throw new BadRequestException("ID does not exist."); 
		}
		
		return book;
	}
	
	@PostMapping("/books")
	public ResponseEntity<Book> addBook(@RequestBody @Valid Book book) throws URISyntaxException {
		if (validateBookIsbnEmpty(book.getIsbn())) {
			throw new BadRequestException("ISBN is required."); 
		}

		List<Author> savedAuthors = authorService.saveAll(book.getAuthors());
		book.setAuthors(savedAuthors);

		
		Book dbBook = bookService.save(book);
		URI uri =  new URI("/api/books/" + dbBook.getId());

		return ResponseEntity.created(uri).body(dbBook);
	}
	
	@GetMapping("/books/search")
	public List<Book> searchForBook(@RequestParam(required = false) String title, 
								@RequestParam(required = false) String authorName) {
		
		List<Book> books = new ArrayList<Book>();
		
		if (title != null && authorName != null) {
			books = bookService.findByTitleAndAuthorName(title, authorName);
		} else if (title != null) {
			books = bookService.findByTitle(title);
		} else if (authorName != null) {
			books = bookService.findByAuthorName(authorName);
		}
		
		return books;
	}
	
	@PutMapping("/books/{id}")
	public Book updateBook(@PathVariable int id, @Valid @RequestBody Book book) {
		Book dbBookById = bookService.findById(id);
        if (dbBookById == null) {
            throw new ResourceNotFoundException("Book not found.");
        }
        
        if (validateBookIsbnEmpty(book.getIsbn())) {
			throw new BadRequestException("ISBN is required."); 
		}
        
        List<Author> savedAuthors = authorService.saveAll(book.getAuthors());
      	
        dbBookById.setAuthors(savedAuthors);
        dbBookById.setIsbn(book.getIsbn());
        dbBookById.setGenre(book.getGenre());
        dbBookById.setPrice(book.getPrice());
        dbBookById.setTitle(book.getTitle());
        dbBookById.setYear(book.getYear());
        
		Book dbBook = bookService.save(dbBookById);
		
		return dbBook;
	}
	
	@DeleteMapping("/books/{id}")
	public ResponseEntity<ApiResponse> deleteBook(@PathVariable int id) {
		Book existingBook = bookService.findById(id);
		
		if (existingBook == null) {
			throw new BadRequestException("Book not found.");
		}
		
		bookService.deleteById(id);
		
		ApiResponse response = new ApiResponse();
		response.setStatus(HttpStatus.OK.value());
		response.setMessage("Book has been deleted successfully.");
		response.setTimestamp(System.currentTimeMillis());

		return ResponseEntity.ok(response);
	}
	
	private boolean validateBookIsbnEmpty(String isbn) {
		if (isbn == null) {
			return true;
		}
		
		if (isbn.isEmpty()) {
			return true;
		}
		
		return false;
	}
}
