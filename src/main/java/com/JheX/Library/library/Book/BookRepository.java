package com.JheX.Library.library.Book;

import com.JheX.Library.library.Author.Author;
import com.JheX.Library.library.PublishingHouse.PublishingHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface BookRepository extends CrudRepository<Book, Long>, JpaRepository<Book, Long> {
    List<Book> findByAuthor(Author author);
    List<Book> findByPublishingHouse(PublishingHouse publishingHouse);
    //Book findByTitleAndDescriptionAndNumberOfPagesAndDateAddedAndUpdatedAtAndAuthorAndEditorial(String title, String description, Integer numberOfPages, Set<BookGenres> genres, LocalDate dateAdded, LocalDateTime updatedAt, Author author,PublishingHouse publishingHouse);
}
