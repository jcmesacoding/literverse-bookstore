package com.jumedev.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table(name = "books")
public class DatosBook {
    @Id
    private int idBook;
    private String title;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "idBook"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id")
    )
    private List<Author> authors;

    private int download_count;
    private String language;


    public int getId() {
        return idBook;
    }

    public void setId(int idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        StringBuilder authorsString = new StringBuilder();

        if (authors != null && !authors.isEmpty()) {
            for (Author author : authors) {
                authorsString.append(author.toString()).append("; ");
            }
            if (authorsString.length() >= 2) {
                authorsString.setLength(authorsString.length() - 2);
            }
        } else {
            authorsString.append("Autor desconocido");
        }

        return String.format("ID: %d | Título: %s | Autor(es): %s | Descargas: %d",
                idBook, title, authorsString, download_count);
    }
}


