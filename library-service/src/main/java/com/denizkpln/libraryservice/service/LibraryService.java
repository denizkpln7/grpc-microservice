package com.denizkpln.libraryservice.service;

import com.denizkpln.libraryservice.model.Library;
import com.denizkpln.libraryservice.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public Library save(Library library) {
        return libraryRepository.save(library);
    }

    public Library findById(long id) {
        return libraryRepository.findById(id).orElseThrow(()->new RuntimeException("böyle bir idli bir library yok"));
    }

    public List<Library> findAll() {
        return libraryRepository.findAll();
    }
}
