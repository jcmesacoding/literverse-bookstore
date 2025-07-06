package com.jumedev.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiBook(
        int id,
        String title,
        List<ApiAuthor> authors,
        int download_count,
        List<String> languages
){}