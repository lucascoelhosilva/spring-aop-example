package com.coelho.springaopexample.repositories;

import com.coelho.springaopexample.models.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Repository
public class BookRepository {

    private final HashMap<UUID, Book> data = new HashMap<>();

    public Book save(Book book) {
        data.put(book.getId(), book);
        return book;
    }

    public Collection<Book> findAll() {
        return data.values();
    }

    public Book findById(UUID id) {
        return data.get(id);
    }

    public void deleteById(UUID id) {
        data.remove(id);
    }

    public void deleteAll() {
        data.clear();
    }
}
