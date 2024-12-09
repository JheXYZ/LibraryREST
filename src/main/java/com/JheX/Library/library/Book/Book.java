package com.JheX.Library.library.Book;

import com.JheX.Library.library.Author.Author;
import com.JheX.Library.library.Book.Genres.BookGenres;
import com.JheX.Library.library.PublishingHouse.PublishingHouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="books")
public class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        @NotEmpty
        private String title;

        @Lob
        private String description;

        @PositiveOrZero
        @ColumnDefault("0")
        private Integer numberOfPages;

        @ElementCollection
        @Enumerated(EnumType.STRING)
        private Set<BookGenres> genres;

        @Column(name = "date_of_creation", nullable = false, updatable = false)
        @CreationTimestamp
        private LocalDate dateAdded;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @ManyToOne
        private Author author;

        @ManyToOne
        private PublishingHouse publishingHouse;

        public Book (){
        }

        public Book(String title, String description, Integer numberOfPages, Set<BookGenres> genres, Author author, PublishingHouse publishingHouse) {
                this.title = title;
                this.description = description;
                this.numberOfPages = numberOfPages;
                this.genres = genres;
                this.author = author;
                this.publishingHouse = publishingHouse;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public Integer getNumberOfPages() {
                return numberOfPages;
        }

        public void setNumberOfPages(Integer numberOfPages) {
                this.numberOfPages = numberOfPages;
        }

        public Set<BookGenres> getGenres() {
                return genres;
        }

        public void setGenres(Set<BookGenres> genres) {
                this.genres = genres;
        }

        public LocalDate getDateAdded() {
                return dateAdded;
        }

        public void setDateAdded(LocalDate dateAdded) {
                this.dateAdded = dateAdded;
        }

        public LocalDateTime getUpdatedAt() {
                return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
        }

        public Author getAuthor() {
                return author;
        }

        public void setAuthor(Author author) {
                this.author = author;
        }

        public PublishingHouse getPublishingHouse() {
                return publishingHouse;
        }

        public void setPublishingHouse(PublishingHouse publishingHouse) {
                this.publishingHouse = publishingHouse;
        }

        @Override
        public String toString() {
                return "Book{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", description='" + description + '\'' +
                        ", numberOfPages=" + numberOfPages +
                        ", genres=" + genres +
                        ", dateAdded=" + dateAdded +
                        ", updatedAt=" + updatedAt +
                        ", author=" + author +
                        ", editorial=" + publishingHouse +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Book book = (Book) o;
                return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(description, book.description) && Objects.equals(numberOfPages, book.numberOfPages) && Objects.equals(genres, book.genres) && Objects.equals(dateAdded, book.dateAdded) && Objects.equals(updatedAt, book.updatedAt) && Objects.equals(author, book.author) && Objects.equals(publishingHouse, book.publishingHouse);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, title, description, numberOfPages, genres, dateAdded, updatedAt, author, publishingHouse);
        }
}
