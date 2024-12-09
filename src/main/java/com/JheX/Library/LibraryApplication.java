package com.JheX.Library;

import com.JheX.Library.library.Author.Author;
import com.JheX.Library.library.Author.AuthorService;
import com.JheX.Library.library.Book.Book;
import com.JheX.Library.library.Book.BookService;
import com.JheX.Library.library.PublishingHouse.PublishingHouse;
import com.JheX.Library.library.PublishingHouse.PublishingHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/*
	This project was made by Nicolas Rodriguez Briz, nickname: JheX
	This is library REST project where you have Books, Authors of the Books and Publishing Houses.
	To make use of the REST API in this project you would need a browser or (preferably) PostMan to make the calls.
	In the browser you can call the GET and DELETE methods of Authors, Publishing Houses and Books.
	But in PostMan you can use all the CRUD operations (POST, GET, PUT, PATCH, DELETE)
	The base URL is: http://localhost:8095/
	The Authors URL is "api/authors":
		"api/authors": Supports GET and POST method.
			-GET: returns all the authors.
			-POST: expects a JSON object in the body, if it's not already in the database it saves it. Expected JSON fields:
				{
					"name": "your author name", 			-> must not be null or blank
					"lastName": "your author last name"		-> this is optional
				}
		"api/authors/{id}": Supports GET, PUT, PATCH, DELETE methods, needs the ID in the URI:
			-GET: return the author if present, otherwise returns code 404 not_found
			-PUT: expects the ID on the URI and a JSON object in the body, then it overwrites the corresponding author (all the fields should be filled).
				If all goes well it returns code 204 no_content, otherwise 400 bad_request
				e.g:
				{
					"name": "your author name", 			-> must not be null or blank
					"lastName": "your author last name"
				}
			-PATCH: expects the ID on the URI and a JSON object in the body, then it only updates the fields provided.
				If all goes well it returns code 204 no_content, otherwise 400 bad_request
				{
					"name": "your author name", 			-> must not be null or blank | this is optional
					"lastName": "your author last name"		-> this is optional
				}
			-DELETE: deletes the author if present, otherwise returns code 404 not_found, needs the ID in the URI.
	The Publishing Houses URL is "api/publishing-houses":
		Supports all the CRUD methods except PATCH since its redundant due to PUT, is the same as the Authors but with only one filed, the expected JSON field is:
			{
				"name": "your publishing house name"		-> must not be null or blank
			}
	The Books URS is "api/books":
		Supports all the CRUD, it's the same as the others but the Author and Publishing House fields should be objects, not IDs.
		The books have genres and are store in a enum, so in the JSON, the genres should be in uppercase, the genres are:
		[FANTASY, SCIENCE_FICTION, DYSTOPIAN, ROMANCE, MYSTERY, THRILLER, HORROR, BIOGRAPHY, AUTOBIOGRAPHY, INSPIRATIONAL,
		RELIGIOUS, SELF_HELP, ADVENTURES, HISTORICAL, FICTION, COMEDY, POETRY, CLASSIC_LITERATURE, CRIME, DETECTIVE, CUISINE,
		CHILDREN_LITERATURE, MAGIC, NOVEL, FANTASY_LITERATURE]
		If you would like to add more genres then modify com.JheX.Library.library.Book.Genres.BookGenres enum
		The expected JSON fields are:
			{
				"title": "your title",						-> must not be null or blank
				"description": "your description",			-> this is optional
				"numberOfPages": number(int),				-> must be >= 0 | default: 0
				"genres": ["GENRE_1", "GENRE_2"],			-> this is optional
				"author": {									-> this is optional | if included, all the fields must be filled
					"id": number(long),
					"name": "your author name",
					"lastName": "your author last name"
				},
				"publishingHouse":{							-> this is optional | if included, all the fields must be filled
					"id": number(long),
					"name": "Real Publishing House 1"
				}
			}
*/
@SpringBootApplication
public class LibraryApplication {
	private static final Logger log = LoggerFactory.getLogger(LibraryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	//To make it simple I decided to use an in memory database (H2 database), so every time the server is closed all the records are lost.
	//Therefore it needs to filled itself again. Of course this is for this exercises only, in a persistence database this wouldn't be needed.
	//The following is only executed at the start of the server once.
	@Bean
	CommandLineRunner fillDatabase(BookService bookService, AuthorService authorService, PublishingHouseService publishingHouseService){
		return args -> {
			int quantity = 100;
			long time = System.nanoTime();
			log.info("Initializing the Library...");
			log.info("Generating Authors...");
			List<Author> authors = authorService.generateAuthors(quantity);
			try {
				authorService.saveAll(authors);
			} catch (Exception e) {
                log.error("Something went wrong when saving the Authors: \n{}", e.toString());
			}
			log.info("Authors saved successfully!");
			log.info("Generating Publishing Houses...");
			List<PublishingHouse> publishingHouses = publishingHouseService.generatePublishingHouses(quantity);
			try {
				publishingHouseService.saveAll(publishingHouses);
			} catch (Exception e) {
				log.error("Something went wrong when saving the Publishing Houses: \n{}", e.toString());
			}
			log.info("Publishing Houses saved successfully!");
			log.info("Generating Books...");
			List<Book> books = bookService.generateBooks(authors, publishingHouses, quantity);
			try {
				bookService.saveAll(books);
			} catch (Exception e) {
				log.error("Something went wrong when saving the Books: \n{}", e.toString());
			}
			log.info("Books saved successfully!");
			time = System.nanoTime() - time;
            log.info("Initialization Completed Successfully! Time: {}ms", time / 1_000_000D);
		};
	}


}
