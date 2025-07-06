package com.jumedev.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiAuthor(
        String name,
        Integer birth_year,
        Integer death_year
) {}
