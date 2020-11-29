package com.example.cacheDemo.services;

import com.example.cacheDemo.entities.Book;
import com.example.cacheDemo.repositories.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public Book addBook(Book book){
        log.info("adding book with id - {}", book.getId());
        return bookRepository.save(book);
    }

    //o obtii din cache si ii faci un update
    //folosind instructiunea cauta in cache si nu mai cauta direct in baza de date
    @CachePut(cacheNames = "books",key = "#book.id")
    public Book updateBook(Book book){
        bookRepository.updateAddress(book.getId(), book.getName());
        log.info("book updated with new name");
        return book;
    }

    //o pui in cache
    @Cacheable(cacheNames = "books",key = "#id")
    public Book getBook(long id){
        log.info("fetching book from db");
        Optional<Book> book=bookRepository.findById(id);
        if(book.isPresent()){
            return book.get();
        }else
            return new Book();
    }

    @CacheEvict(cacheNames = "books",key = "#id")
    public String deleteBook(long id){
        bookRepository.deleteById(id);
        return "Book deleted";
    }

}