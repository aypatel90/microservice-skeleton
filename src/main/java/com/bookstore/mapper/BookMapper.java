package com.bookstore.mapper;

import com.bookstore.dto.BooksDto;
import com.bookstore.model.Book;
import lombok.Data;

@Data
public class BookMapper {

    private BookMapper() {

    }

    public static BooksDto mapToBooksDto(Book book, BooksDto booksDto) {
        booksDto.setId(book.getId());
        booksDto.setTitle(book.getTitle());
        booksDto.setAuthor(book.getAuthor());
        booksDto.setEmail(book.getEmail());
        return booksDto;
    }

    public static Book mapToBook(BooksDto booksDto, Book book) {
        book.setTitle(booksDto.getTitle());
        book.setAuthor(booksDto.getAuthor());
        book.setEmail(booksDto.getEmail());
        return book;
    }

}
