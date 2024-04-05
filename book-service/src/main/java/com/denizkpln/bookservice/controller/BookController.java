package com.denizkpln.bookservice.controller;


import com.denizkpln.bookservice.dto.BookDto;
import com.denizkpln.bookservice.dto.LibraryDto;
import com.denizkpln.bookservice.model.Book;
import com.denizkpln.bookservice.service.BookGrpcService;
import com.denizkpln.bookservice.service.BookService;
import com.google.protobuf.Descriptors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    private final BookGrpcService bookGrpcService;

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody Book book){
        log.info("product save");
        return ResponseEntity.ok(bookService.save(book));
    }

    @GetMapping(value = "bookbyid/{id}")
    public ResponseEntity<BookDto> bookById(@PathVariable Long id){
        log.info("product save");
        return ResponseEntity.ok(bookService.bookById(id));
    }

    @GetMapping(value = "librarylist")
    public ResponseEntity<List<Map<Descriptors.FieldDescriptor, Object>>> listLibrary() throws InterruptedException {
        log.info("library list");
        return ResponseEntity.ok(bookService.listLibrary());
    }

    @GetMapping("/book")
    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getActiveLibrary() throws InterruptedException {
        return bookGrpcService.getExpensiveBook();
    }

}
