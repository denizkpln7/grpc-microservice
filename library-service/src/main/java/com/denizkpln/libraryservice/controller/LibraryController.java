package com.denizkpln.libraryservice.controller;



import com.denizkpln.libraryservice.model.Library;
import com.denizkpln.libraryservice.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping
    public ResponseEntity<Library> save(@RequestBody Library library){
        log.info("product save");
        return ResponseEntity.ok(libraryService.save(library));
    }


}
