package com.example.libreria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BooksResponse {
    private Book[] results;

    public Book[] getResults() { return results; }
    public void setResults(Book[] results) { this.results = results; }
}