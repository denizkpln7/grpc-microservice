package com.denizkpln.bookservice.service;

import com.denizkpln.bookservice.dto.BookDto;
import com.denizkpln.bookservice.dto.LibraryDto;
import com.denizkpln.bookservice.model.Book;
import com.denizkpln.bookservice.repository.BookRepository;
import com.example.grpccommon.GetLibrary;
import com.google.protobuf.Descriptors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookGrpcService bookGrpcService;

    public Book save(Book book) {
        return bookRepository.save(book);
    }


    public BookDto bookById(Long id) {
        Book book=bookRepository.findById(id).orElseThrow(()->new RuntimeException("Not found"));
        GetLibrary getLibrary=bookGrpcService.getLibrary(book.getLibrary_id());
        return BookDto.builder().book_name(book.getName()).library_name(getLibrary.getName()).build();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> listLibrary() throws InterruptedException {
        return bookGrpcService.getAllLibrary();
    }
}
