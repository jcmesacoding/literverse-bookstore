package com.jumedev.bookstore.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.jumedev.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LanguageUpdater {

    @Autowired
    private BookRepository bookRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public void updateLanguages() {
        // Trae todos los libros sin idioma
        List<DatosBook> librosSinIdioma = bookRepository.findByLanguageIsNull();

        for (DatosBook book : librosSinIdioma) {
            int id = book.getId();
            String url = "https://gutendex.com/books/" + id;

            try {
                JsonNode response = restTemplate.getForObject(url, JsonNode.class);
                JsonNode languagesNode = response.get("languages");

                if (languagesNode != null && languagesNode.isArray() && !languagesNode.isEmpty()) {
                    String language = languagesNode.get(0).asText();
                    book.setLanguage(language);
                    bookRepository.save(book);
                    System.out.println("✅ Libro actualizado: " + book.getTitle() + " -> " + language);
                } else {
                    System.out.println("❌ No se encontró idioma para: " + book.getTitle());
                }

            } catch (Exception e) {
                System.out.println("⚠️ Error al procesar el libro ID " + id + ": " + e.getMessage());
            }
        }
    }
}
