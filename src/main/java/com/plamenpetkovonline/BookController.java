package com.plamenpetkovonline;

import com.plamenpetkovonline.exception.*;
import com.plamenpetkovonline.service.Book;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/api/books")
public class BookController {

    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/g={genre}")
    public List<Book> getGenre(@PathVariable(value="genre") String genre) {
        verifyBookGenreExists(genre);
        Iterable<Book> allBooks = repository.findAll();
        List<Book> result = StreamSupport.stream(allBooks.spliterator(), false)
                .filter(x -> x.getGenre().toLowerCase().equals(genre.toLowerCase()))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/a={author}")
    public List<Book> getAuthor(@PathVariable(value="author") String author) {
        verifyBookAuthorExist(author);
        Iterable<Book> allBooks = repository.findAll();
        List<Book> result = StreamSupport.stream(allBooks.spliterator(), false)
                .filter(x -> x.getAuthor().toLowerCase().equals(author.replace("%20", " ").toLowerCase()))
                .collect(Collectors.toList());
        return result;
    }
    @GetMapping("/y={year}")
    public List<Book> getYear(@PathVariable(value="year") String year) {
        verifyBookYearExists(year);
        Iterable<Book> allBooks = repository.findAll();
        List<Book> result = StreamSupport.stream(allBooks.spliterator(), false)
                .filter(x -> x.getYear().trim().equals(year.trim()))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/t={title}")
    public List<Book> getTitle(@PathVariable(value="title") String title) {
        verifyBookTitleExists(title);
        Iterable<Book> allBooks = repository.findAll();
        List<Book> result = StreamSupport.stream(allBooks.spliterator(), false)
                .filter(x -> x.getTitle().toLowerCase().equals(title.replace("%20", " ").toLowerCase()))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable(value="id") Integer id) {
        verifyBookExists(id);

        return repository.findById(id).get();
    }

    @GetMapping
    public List<Book> getAll() {
        Spliterator<Book> books = repository.findAll()
                .spliterator();

        return StreamSupport
                .stream(books, false)
                .collect(Collectors.toList());
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book post(@RequestBody(required = false) Book book) {
        verifyCorrectPayload(book);

        return repository.save(book);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Book put(@PathVariable("id") Integer id, @RequestBody(required = false) Book book) {
        verifyBookExists(id);
        verifyCorrectPayload(book);

        book.setId(id);
        return repository.save(book);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        verifyBookExists(id);

        repository.deleteById(id);
    }

    // Exception methods below

    private void verifyBookExists(Integer id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(String.format("Book with id=%d was not found", id));
        }
    }

    private void verifyCorrectPayload(Book book) {
        if (Objects.isNull(book)) {
            throw new UnsupportedMediaTypeException("Book cannot be null");
        }

        if (Objects.isNull(book.getAuthor()) || book.getAuthor().trim().length() == 0) {
            throw new UnprocessableEntityException("The author is required!");
        }

        if (Objects.isNull(book.getYear()) || book.getYear().trim().length() == 0) {
            throw new UnprocessableEntityException("The year is required!");
        }

        if (Objects.isNull(book.getGenre()) || book.getGenre().trim().length() == 0) {
            throw new UnprocessableEntityException("The genre is required!");
        }

        if (Objects.isNull(book.getTitle()) || book.getTitle().trim().length() == 0) {
            throw new UnprocessableEntityException("The title is required!");
        }

        if (!Objects.isNull(book.getId())) {
            throw new UnprocessableEntityException("Id field must be generated");
        }
    }

    private void verifyBookGenreExists(String input) {
        if (!StreamSupport.stream(repository.findAll().spliterator(), false).anyMatch(g -> g.getGenre().toLowerCase().equals(input.replace("%20"," ").toLowerCase()))) {
            throw new NotFoundException(String.format("Book with genre=%s was not found", input));
        }
    }


    private void verifyBookAuthorExist(String author) {
        if (!StreamSupport.stream(repository.findAll().spliterator(), false).anyMatch(g -> g.getAuthor().toLowerCase().equals(author.replace("%20"," ").toLowerCase()))) {
            throw new NotFoundException(String.format("Book with author=%s was not found", author));
        }
    }

    private void verifyBookYearExists(String year) {
        if (!StreamSupport.stream(repository.findAll().spliterator(), false).anyMatch(g -> g.getYear().trim().equals(year.trim().toLowerCase()))) {
            throw new NotFoundException(String.format("Book from year=%s was not found", year));
        }
    }

    private void verifyBookTitleExists(String title) {
        if (!StreamSupport.stream(repository.findAll().spliterator(), false).anyMatch(g -> g.getTitle().toLowerCase().equals(title.replace("%20"," ").toLowerCase()))) {
            throw new NotFoundException(String.format("Book with title=%s was not found", title));
        }
    }

}