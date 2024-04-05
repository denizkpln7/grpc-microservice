package com.denizkpln.bookservice.repository;

import com.denizkpln.bookservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
