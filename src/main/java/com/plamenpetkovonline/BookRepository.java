package com.plamenpetkovonline;

import com.plamenpetkovonline.service.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findByGenre(String g);
}