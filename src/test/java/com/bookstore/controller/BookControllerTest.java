// Purpose: To test the controller layer (web layer).
package com.bookstore.controller;

import com.bookstore.controller.BookController;
import com.bookstore.dto.BooksDto;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(BookController.class)
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenPostBook_thenBookShouldBeCreated() throws Exception {
        Book book = new Book("Test Title", "Test Author","ankitpatel3190@gmail.com");
        when(bookService.saveBook(any(BooksDto.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }
}

