package com.JheX.Library.library.PublishingHouse;

import com.JheX.Library.library.Book.Book;
import com.JheX.Library.library.Book.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublishingHouseService {
    private static final Logger log = LoggerFactory.getLogger(PublishingHouseService.class);
    private final PublishingHouseRepository PHRepository;
    private final BookRepository bookRepository;

    public PublishingHouseService(PublishingHouseRepository PHRepository, BookRepository bookRepository) {
        this.PHRepository = PHRepository;
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<List<PublishingHouse>> getAll() {
        return ResponseEntity.ok(PHRepository.findAll());
    }

    public ResponseEntity<PublishingHouse> findById(Long id) {
        Optional<PublishingHouse> publishingHouseResponse = PHRepository.findById(id);
        return publishingHouseResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<PublishingHouse> createPublishingHouse(PublishingHouse publishingHouse) {
        PublishingHouse publishingHoseSearch = PHRepository.findByName(publishingHouse.getName());
        if (publishingHoseSearch != null) //if it already exists then return the existing PH, therefore not creating a new one
            return new ResponseEntity<>(publishingHoseSearch, HttpStatus.CONFLICT);

        Long publishingHouseID = PHRepository.save(publishingHouse).getId();
        URI pathOfNewPH = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(publishingHouseID)
                .toUri();
        return ResponseEntity.created(pathOfNewPH).build();
    }

    public ResponseEntity<Void> putPublishingHouse(Long requestedId, PublishingHouse publishingHouseUpdate) {
        Optional<PublishingHouse> PHResponse = PHRepository.findById(requestedId);
        if (PHResponse.isEmpty())
            return ResponseEntity.notFound().build();

        publishingHouseUpdate.setId(requestedId);
        if (publishingHouseUpdate.equals(PHResponse.get()))
            return ResponseEntity.noContent().build();

        PHRepository.save(publishingHouseUpdate);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deletePublishingHouse(Long id) {
        Optional<PublishingHouse> PHresponse = PHRepository.findById(id);
        if (PHresponse.isEmpty())
            return ResponseEntity.notFound().build();

        List<Book> books = bookRepository.findByPublishingHouse(PHresponse.get());
        if (books != null) {
            for (Book book: books)
                book.setPublishingHouse(null);
            bookRepository.saveAll(books);
        }
        PHRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public List<PublishingHouse> generatePublishingHouses(int quantity){
        List<PublishingHouse> publishingHouses = new ArrayList<>();
        for (int i=1 ; i <= quantity ; i++)
            publishingHouses.add(new PublishingHouse("Real Publishing House " + i));
        return publishingHouses;
    }

    public void saveAll(List<PublishingHouse> PH){
        PHRepository.saveAll(PH);
    }

}








