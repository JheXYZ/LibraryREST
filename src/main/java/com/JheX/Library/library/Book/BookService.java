package com.JheX.Library.library.Book;

import com.JheX.Library.library.Author.Author;
import com.JheX.Library.library.Author.AuthorRepository;
import com.JheX.Library.library.Book.Genres.BookGenres;
import com.JheX.Library.library.PublishingHouse.PublishingHouse;
import com.JheX.Library.library.PublishingHouse.PublishingHouseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublishingHouseRepository phRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublishingHouseRepository phRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.phRepository = phRepository;
    }

    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    public ResponseEntity<Book> findById(Long id) {
        Optional<Book> bookResponse = bookRepository.findById(id);
        return bookResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Book> createBook(Book book) {
        if (book == null)
            return ResponseEntity.badRequest().build();
        if (!isAuthorValidOrIsPublishingHouseValid(book))
            return ResponseEntity.badRequest().build();

        Long savedBookId = bookRepository.save(book).getId();
        URI pathOfNewBook = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBookId)
                .toUri();
        return ResponseEntity.created(pathOfNewBook).build();   //returns the URI in the header/location of where it was saved, e.g: http://localhost:8095/api/books/101
    }

    public ResponseEntity<Void> putBook(Long requestedId, Book book) {
        Optional<Book> bookResponse = bookRepository.findById(requestedId);
        if (bookResponse.isEmpty())
            return ResponseEntity.notFound().build();
        if (book.getAuthor() != null && !authorExistsAndIsValid(book.getAuthor()))
            return ResponseEntity.badRequest().build();
        if (book.getPublishingHouse() != null && !publishingHouseExistsAndIsValid(book.getPublishingHouse()))
            return ResponseEntity.badRequest().build();

        book.setId(requestedId);
        if (bookResponse.get().equals(book))
            return ResponseEntity.noContent().build();

        bookRepository.save(book);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> patchBook(Long requestedId, Book bookPatch){
        Optional<Book> bookResponse = bookRepository.findById(requestedId);
        if (bookResponse.isEmpty())
            return ResponseEntity.notFound().build();

        //checks if the author or the publishing house aren't null, if so checks if any of there attributes are null if so return 400 bad_request, otherwise continues
        if (!isAuthorValidOrIsPublishingHouseValid(bookResponse.get()))
            return ResponseEntity.badRequest().build();

        bookPatch.setId(requestedId);
        if (bookResponse.get().equals(bookPatch))
            return ResponseEntity.noContent().build();

        bookRepository.save(patchedBook(bookResponse.get(), bookPatch));
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteBook(Long id) {
        if (!bookRepository.existsById(id))
            return ResponseEntity.notFound().build();

        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Book patchedBook(Book book, Book patchBook){
        if (patchBook.getTitle() != null && !patchBook.getTitle().isBlank())
            book.setTitle(patchBook.getTitle());
        if (patchBook.getDescription() != null)
            book.setDescription(patchBook.getDescription());
        if (patchBook.getNumberOfPages() != null && patchBook.getNumberOfPages() >= 0)
            book.setNumberOfPages(patchBook.getNumberOfPages());
        if (patchBook.getGenres() != null)
            book.setGenres(patchBook.getGenres());
        if (patchBook.getAuthor() != null && !patchBook.getAuthor().equals(book.getAuthor()))
            book.setAuthor(patchBook.getAuthor());
        if (patchBook.getPublishingHouse() != null && !patchBook.getPublishingHouse().equals(book.getPublishingHouse()))
            book.setPublishingHouse(patchBook.getPublishingHouse());
        return book;
    }

    private boolean authorExistsAndIsValid(Author author){
        if (author == null)
            return false;
        if (author.getId() == null)
            return false;
        Optional<Author> authorResponse = authorRepository.findById(author.getId());
        return authorResponse.isPresent() && authorResponse.get().equals(author);
    }

    private boolean publishingHouseExistsAndIsValid(PublishingHouse publishingHouse){
        if (publishingHouse == null)
            return false;
        if (publishingHouse.getId() == null)
            return false;
        Optional<PublishingHouse> publishingHouseResponse = phRepository.findById(publishingHouse.getId());
        return publishingHouseResponse.isPresent() && publishingHouseResponse.get().equals(publishingHouse);
    }

    private boolean isAuthorValidOrIsPublishingHouseValid(Book book){
        if (book.getAuthor() != null && !authorExistsAndIsValid(book.getAuthor()))
            return false;
        return book.getPublishingHouse() == null || publishingHouseExistsAndIsValid(book.getPublishingHouse());
    }

    //used only for initialization of the database
    public List<Book> generateBooks(List<Author> authorList, List<PublishingHouse> publishingHouseList, int quantity){
        Collections.shuffle(authorList);
        Collections.shuffle(publishingHouseList);
        List<Book> bookList = new ArrayList<>();
        Random random = new Random();
        int bookGenreQuantity = BookGenres.values().length;

        for (int i=1 ; i <= quantity ; i++)
            bookList.add(new Book(
                    "Book " + i, "Description N°" + i, random.nextInt(0, 1000),
                    getRandomGenres(random.nextInt(0, bookGenreQuantity)),
                    authorList.get(random.nextInt(0, authorList.size())),
                    publishingHouseList.get(random.nextInt(0, publishingHouseList.size()))
            ));
        return bookList;
    }
    //used only for initialization of the database
    private Set<BookGenres> getRandomGenres(int quantity){
        ArrayList<BookGenres> genresList = new ArrayList<>(EnumSet.allOf(BookGenres.class));
        Collections.shuffle(genresList);
        Set<BookGenres> randomGenres = new HashSet<>();

        for (int i=0 ; i < Math.min(quantity, genresList.size()) ; i++)
            randomGenres.add(genresList.get(i));
        return randomGenres;
    }
    //used only for initialization of the database
    public void saveAll(List<Book> books) {
        bookRepository.saveAll(books);
    }
}
