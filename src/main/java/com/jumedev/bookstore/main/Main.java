package com.jumedev.bookstore.main;

import com.jumedev.bookstore.model.Author;
import com.jumedev.bookstore.model.DatosBook;
import com.jumedev.bookstore.model.GutendexResponse;
import com.jumedev.bookstore.repository.AuthorRepository;
import com.jumedev.bookstore.repository.BookRepository;
import com.jumedev.bookstore.service.ConsumoApi;
import com.jumedev.bookstore.service.ConvierteDatos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConvierteDatos conversor = new ConvierteDatos();

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        var json = consumoApi.obtenerDatos("https://gutendex.com/books/?search=");
//        System.out.println(json);

        if (json == null || json.isBlank()) {
            System.out.println("Error: La API devolvió contenido vacío. Revisa tu conexión o la URL.");
            return;
        }
        var datos = conversor.obtenerDatos(json, GutendexResponse.class);
//        System.out.println(datos.getResults());

        while(true) {
            System.out.print("""
                    **Welcome to LiterVerse**
                    
                    ***************************************
                    1- Search Book by Title
                    2- List Book Registered
                    3- List Author Registered
                    4- List Author Alive Per Years
                    5- List Books by Language
                    6- Top 10 Books by downloads
                    0- Exit
                    ***************************************
                    """);
            System.out.print("Select the desired option: ");
            String userBook = scanner.nextLine();


            switch (userBook) {
                case "1":
                    searchBookByTitle();
                    break;
                case "2":
                    listBookRegistered();
                    break;
                case "3":
                    listAuthorRegistered();
                    break;
                case "4":
                    authorPerYear();
                    break;
                case "5":
                    listBookByLang();
                    break;
                case "6":
                    topTenBooks();
                    break;
                case "0":
                    System.out.println("Leaving the program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid Option. Try Again.");
            }
        }
    }


    private void searchBookByTitle() {
        System.out.print("Write the name of the book: ");
        String userBook = scanner.nextLine().trim();

        if (userBook.isEmpty()) {
            System.out.println("El título no puede estar vacío. Intenta de nuevo.");
            return;
        }

        String URL_BASE = "https://gutendex.com/books/?search=";
        String json = consumoApi.obtenerDatos(URL_BASE + userBook.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, GutendexResponse.class);

        if (datos != null && datos.getResults() != null && !datos.getResults().isEmpty()) {
            DatosBook libro = (DatosBook) datos.getResults().getFirst();

            // Convertir autores API -> entidades JPA
            List<Author> authorEntities = libro.getAuthors().stream()
                    .map(apiAuthor -> {
                        return authorRepository
                                .findByNameAndBirthYearAndDeathYear(apiAuthor.getName(), apiAuthor.getBirthYear(), apiAuthor.getDeathYear())
                                .orElseGet(() -> authorRepository.save(new Author(
                                        apiAuthor.getName(),
                                        apiAuthor.getBirthYear(),
                                        apiAuthor.getDeathYear()
                                )));
                    })
                    .collect(Collectors.toList());

            libro.setAuthors(authorEntities); // Asigna los autores ya convertidos

            System.out.println("Libro encontrado:");
            System.out.println(libro);

            bookRepository.save(libro); // <-- Guarda todo en la base de datos
            System.out.println("Libro guardado en la base de datos.");
        } else {
            System.out.println("No se encontraron libros para esa búsqueda.");
        }
    }

    private void listBookRegistered() {
        List<DatosBook> booksRegistered = bookRepository.findAll();

        if (booksRegistered.isEmpty()) {
            System.out.println("No hay libros guardados en la base de datos aun.");
        } else {
            System.out.println("Libros Guardados:");
            for (DatosBook book : booksRegistered) {
                System.out.println("ID: " + book.getId());
                System.out.println("Título: " + book.getTitle());
                System.out.println("Descargas: " + book.getDownload_count());
                System.out.println("Language: " + book.getLanguage());

                System.out.print("Autores: ");
                book.getAuthors().forEach(author ->
                        System.out.println(author.getName())
                );
                System.out.println("\n*******************************");
            }
        }
    }

    private void listAuthorRegistered() {
//        List<Author> authorsRegistered = authorRepository.findAll();
        Set<Author> authorsRegistered = new LinkedHashSet<>(authorRepository.findAll());

        if (authorsRegistered.isEmpty()) {
            System.out.println("No hay autores guardadados en la base de datos aun.");
        } else {
            System.out.println("*********************");
            System.out.println("Autores Registrados:");
            for (Author author : authorsRegistered) {
                System.out.print("Name: " + author.getName());
                System.out.print(" | Birthyear: " + author.getBirthYear());
                System.out.println(" | DeathYear: " + author.getDeathYear());
            }
            System.out.println("*********************\n");

        }
    }

    private void authorPerYear() {
        System.out.print("Start Year: ");
        int startYear = scanner.nextInt();

        System.out.print("End Year: ");
        int endYear = scanner.nextInt();
        scanner.nextLine();

        List<Author> authorsAlive = authorRepository.findAuthorsAliveBetween(startYear, endYear);
        System.out.println("Authors alive between " + startYear + " and " + endYear + ":");
        System.out.println("***********************************");
        authorsAlive.forEach(a -> System.out.println("Autor: " + a.getName()));
        System.out.println("***********************************");
    }

    private void listBookByLang() {
        System.out.println("""
                What book's language are you looking for?
                
                en = english
                es = spanish
                it = italian
                fr = french
                pt = portuguese
                """);
        System.out.print("option: ");
        String lang = scanner.nextLine().toLowerCase();
        List<DatosBook> booksPerLang =  bookRepository.findByLanguage(lang);
        booksPerLang.forEach(b -> System.out.println("Titulo: " + b.getTitle() +
                                                                " | Lang: " + b.getLanguage() +
                                                                " | Downloads: " + b.getDownload_count() +
                                                                " | Author: " + b.getAuthors()));
    }

    private void topTenBooks() {
        var json = consumoApi.obtenerDatos("https://gutendex.com/books/?search=");
        var datos = conversor.obtenerDatos(json, GutendexResponse.class);
        List<DatosBook> datosBooks =  datos.getResults();

        System.out.println("\n***************************************");
        System.out.println("TOP 10 Libros mas Descargados");
        System.out.println("***************************************");

        datosBooks.stream()
                .filter(book -> book.getDownload_count() > 0)
                .sorted((b1, b2) -> Integer.compare(b2.getDownload_count(), b1.getDownload_count()))
                .limit(10)
                .forEach(System.out::println);
    }

}
