package com.bookstore.service;

import com.bookstore.domain.DomainObject;
import com.bookstore.config.KafkaProducer;
import com.bookstore.dto.BooksDto;
import com.bookstore.exception.BusinessException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    KafkaProducer producer;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        Book book = (Book)redisTemplate.opsForHash().get("BOOK",id);
        if(book == null) {
            return bookRepository.findById(id);
        } else {
            return Optional.of(book);
        }
    }

    public Book saveBook(BooksDto booksDto) throws BusinessException {
        Book book = new Book();
        BookMapper.mapToBook(booksDto, book);
        Book persisted = bookRepository.save(book);
        DomainObject obj = new DomainObject();
        obj.setKey("temp-key");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValueAsString(persisted);
            obj.setDataObject(persisted);
            producer.sendFlightEvent(obj);
            redisTemplate.opsForHash().put("BOOK", persisted.getId(), persisted);
            return persisted;
        } catch (JsonProcessingException exception) {
            logger.error("Exception occurred while parsing object: ", exception);
            throw new BusinessException("Exception occurred while parsing object: ");
        }
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

