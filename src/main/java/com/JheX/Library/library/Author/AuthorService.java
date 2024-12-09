package com.JheX.Library.library.Author;

import com.JheX.Library.library.Book.Book;
import com.JheX.Library.library.Book.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    public ResponseEntity<Author> findById(Long id) {
        Optional<Author> authorResponse = authorRepository.findById(id);
        return authorResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Author> createAuthor(Author author) {
        Author authorResponse = authorRepository.findByNameAndLastName(author.getName(), author.getLastName());
        if (authorResponse != null)
            return new ResponseEntity<>(authorResponse, HttpStatus.CONFLICT);

        Long savedAuthorId = authorRepository.save(author).getId();
        URI pathOfNewAuthor = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAuthorId)
                .toUri();
        return ResponseEntity.created(pathOfNewAuthor).build();
    }

    public ResponseEntity<Void> putAuthor(Long requestedId, Author author) {
        Optional<Author> authorResponse = authorRepository.findById(requestedId);
        if (authorResponse.isEmpty())
            return ResponseEntity.notFound().build();

        author.setId(requestedId);
        if (authorResponse.get().equals(author))
            return ResponseEntity.noContent().build();

        authorRepository.save(author);
        return ResponseEntity.noContent().build();
    }

    //not 100% patch but behaves as such
    public ResponseEntity<Void> patchAuthor(Long requestedId, Author author){
        Optional<Author> authorResponse = authorRepository.findById(requestedId);
        if (authorResponse.isEmpty())
            return ResponseEntity.notFound().build();

        authorRepository.save(patchedAuthor(authorResponse.get(), author));
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteAuthor(Long id) {
        Optional<Author> authorResponse = authorRepository.findById(id);
        if (authorResponse.isEmpty())
            return ResponseEntity.notFound().build();

        List<Book> books = bookRepository.findByAuthor(authorResponse.get());
        if (books != null) {
            log.info(String.valueOf(books.size()));
            for (Book book : books)
                book.setAuthor(null);
            bookRepository.saveAll(books);
        }
        authorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Author patchedAuthor (Author priorAuthor, Author newAuthor){
        newAuthor.setId(priorAuthor.getId());
        if (newAuthor.getName() == null || newAuthor.getName().isBlank() && !newAuthor.getName().equals(priorAuthor.getName()))
            newAuthor.setName(priorAuthor.getName());
        if (newAuthor.getLastName() == null || newAuthor.getLastName().isBlank() && !newAuthor.getLastName().equals(priorAuthor.getLastName()))
            newAuthor.setLastName(priorAuthor.getLastName());
        return newAuthor;
    }

    public List<Author> generateAuthors(int quantity){
        List<Author> authors = new ArrayList<>();
        for (int i = 1 ; i <= quantity ; i++)
            authors.add(new Author("Real Author name " + i, "Last name " + i));
        return authors;
    }

    public void saveAll(List<Author> authors) {
        authorRepository.saveAll(authors);
    }
}
