package com.coelho.springaopexample.controllers;

import com.coelho.springaopexample.aspect.Audit;
import com.coelho.springaopexample.models.Book;
import com.coelho.springaopexample.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collection;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    @Audit
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody Book requestBody, BindingResult result) {
        var book = service.create(requestBody);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/books/{id}").buildAndExpand(book.getId()).toUri())
                             .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> getAll() {
        return service.get();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book getById(@PathVariable UUID id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        service.delete();
    }
}
