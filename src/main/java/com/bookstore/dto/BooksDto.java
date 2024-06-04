package com.bookstore.dto;

import lombok.Data;

@Data
public class BooksDto {

    private Long id;
    private String title;
    private String author;
    private String email;
}
