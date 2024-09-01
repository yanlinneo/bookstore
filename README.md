# Bookstore
A RESTful API for managing a bookstore.

## Disclaimer
> [!CAUTION]
> **This project is not intended for use with production data or in a production environment.**
> 
> This is a personal project created for my own learning purposes.

## Overview
A RESTful API for managing a bookstore, featuring endpoints on managing books. Includes a token-based authentication system to secure user access.

**Tech Stack:** Java, Spring Boot, MySQL

## API Endpoints
#### 1. Generate Bearer Token
**`POST`** /auth/token

Generate a JSON Web Tokens (JWT) for authenticated users. This is needed to access the other API
endpoints. The lifetime of this generated JWT is 15 minutes.

#### 2. Find All Books
**`GET`** /api/books

Retrieve all the books.

#### 3. Find Book by ID
**`GET`** /api/books/*{id}*

Retrieve a book based on the Book ID.

#### 4. Find Books by Title and/or Author Name
**`GET`** /api/books/search

Retrieve books based on book title and/or author’s name.

#### 5. Create Book
**`POST`** /api/books

Create a new book with specific details, especially with its ISBN.

#### 6. Update Book
**`PUT`** /api/books/*{id}*

Update the details of a book.

#### 7. Delete Book
**`DELETE`** /api/books/*{id}*

Delete a book based on its ID.

## Database Schema

![ER_Diagram](https://github.com/user-attachments/assets/b1481463-15e5-43ba-9ecc-b6abda1b7217)
