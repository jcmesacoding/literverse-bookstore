package com.jumedev.bookstore.repository;

import com.jumedev.bookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAll();

    Optional<Author> findByNameAndBirthYearAndDeathYear(String name, Integer birthYear, Integer deathYear);

//    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(int startYear, int endYear);

    @Query("SELECT a FROM Author a WHERE a.birthYear <= :endYear AND a.deathYear >= :startYear")
    List<Author> findAuthorsAliveBetween(@Param("startYear") int startYear, @Param("endYear") int endYear);

//    @Query("SELECT a FROM Author a WHERE a.birth_year <= :a && a.death_year >= :a")
//    List<Author> findAllBetweenYear();

//    List<Author> findByBirthYearBetween(int startYear, int endYear);

}
