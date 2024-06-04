package com.bookstore.controller;

import com.bookstore.dto.BooksDto;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.debug("Get Book Method Called: {} ",id);
        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BooksDto booksDto) {
        Book savedBook = bookService.saveBook(booksDto);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BooksDto bookDetails) {
    return bookService.findBookById(id)
        .map(existingBook -> {
            if (bookDetails.getTitle() != null) {
                existingBook.setTitle(bookDetails.getTitle());
            }
            if (bookDetails.getAuthor() != null) {
                existingBook.setAuthor(bookDetails.getAuthor());
            }
            BookMapper.mapToBooksDto(existingBook, bookDetails);
            Book updatedBook = bookService.saveBook(bookDetails);
            return ResponseEntity.ok(updatedBook);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "BookCache", key = "#id")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}

