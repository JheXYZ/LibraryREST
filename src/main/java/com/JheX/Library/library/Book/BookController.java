package com.JheX.Library.library.Book;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {    //Dependency Injection
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId){
        return bookService.findById(bookId);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book){
        return bookService.createBook(book);
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<Void> putBook(@PathVariable Long requestedId, @RequestBody @Valid Book bookUpdate){
        return bookService.putBook(requestedId, bookUpdate);
    }

    @PatchMapping("/{requestedId}")
    public ResponseEntity<Void> patchBook(@PathVariable Long requestedId, @RequestBody Book bookPatch){
        return bookService.patchBook(requestedId, bookPatch);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId){
        return bookService.deleteBook(bookId);
    }
}
