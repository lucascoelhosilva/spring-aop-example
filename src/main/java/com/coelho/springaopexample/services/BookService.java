package com.coelho.springaopexample.services;

import com.coelho.springaopexample.aspect.Audit;
import com.coelho.springaopexample.aspect.LogExecutionTime;
import com.coelho.springaopexample.exception.NotFoundException;
import com.coelho.springaopexample.models.Book;
import com.coelho.springaopexample.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository repository;

    @Audit
    @LogExecutionTime
    public Book create(Book book) {
        log.info("Creating {}", book);
        return repository.save(book);
    }

    @LogExecutionTime
    public Collection<Book> get() {
        log.info("Getting Books");
        return repository.findAll();
    }

    @LogExecutionTime
    public Book get(UUID id) {
        log.info("Getting Book=[{}]", id);

        Book book = repository.findById(id);
        if (book == null) {
            throw new NotFoundException(String.format("Book with '%s' not found", id));
        }

        return book;
    }

    public void deleteById(UUID id) {
        log.info("Deleting Book=[{}]", id);
        repository.deleteById(id);
    }

    public void delete() {
        log.info("Deleting Book");
        repository.deleteAll();
    }

}
