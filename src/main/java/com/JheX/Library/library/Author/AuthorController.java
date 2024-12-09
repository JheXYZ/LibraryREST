package com.JheX.Library.library.Author;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<Author> findById(@PathVariable Long authorId){
        return authorService.findById(authorId);
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody @Valid Author author){
        return authorService.createAuthor(author);
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<Void> putAuthor(@PathVariable Long requestedId, @RequestBody @Valid Author authorUpdate){
        return authorService.putAuthor(requestedId, authorUpdate);
    }

    @PatchMapping("/{requestedId}")
    public ResponseEntity<Void> patchAuthor(@PathVariable Long requestedId, @RequestBody Author authorPatch){
        return authorService.patchAuthor(requestedId, authorPatch);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId){
        return authorService.deleteAuthor(authorId);
    }
}
