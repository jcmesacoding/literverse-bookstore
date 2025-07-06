package com.jumedev.bookstore.repository;

import com.jumedev.bookstore.model.DatosBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<DatosBook, Integer> {
//    @Query("SELECT DISTINCT b FROM DatosBook b LEFT JOIN FETCH b.authors")
//    List<DatosBook> findAllWithAuthors();


    List<DatosBook> findByLanguage(String language);

    List<DatosBook> findByLanguageIsNull();  // ← Agrega esto para buscar libros sin lenguaje asignado
}
