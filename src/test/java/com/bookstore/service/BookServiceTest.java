// Purpose: To test the service layer (business logic).
package com.bookstore.service;

import com.bookstore.dto.BooksDto;
import com.bookstore.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.bookstore.BookStore;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BookStore.class)
@ActiveProfiles("test")
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void whenSaveBook_thenBookShouldBeSaved() {
        Book book = new Book("Test Title", "Test Author","ankitpatel3190@gmail.com");
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        BooksDto dto = new BooksDto();
        BookMapper.mapToBooksDto(book,dto);
        Book savedBook = bookService.saveBook(dto);
        
        assertNotNull(savedBook);
        assertEquals("Test Title", savedBook.getTitle());
    }

    @Test
    public void whenFindById_thenBookShouldBeFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book("Test Title", "Test Author",
                "ankitpatel3190@gmail.com")));
        
        Optional<Book> foundBook = bookService.findBookById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }
}

