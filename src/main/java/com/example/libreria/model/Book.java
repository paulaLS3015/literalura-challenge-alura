package com.example.libreria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String title;
    private String[] languages;
    private int downloads;
    private Author[] authors;

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String[] getLanguages() { return languages; }
    public void setLanguages(String[] languages) { this.languages = languages; }

    public int getDownloads() { return downloads; }
    public void setDownloads(int downloads) { this.downloads = downloads; }

    public Author[] getAuthors() { return authors; }
    public void setAuthors(Author[] authors) { this.authors = authors; }

    @Override
    public String toString() {
        String authorNames = (authors != null && authors.length > 0) ? authors[0].getName() : "Unknown";
        String language = (languages != null && languages.length > 0) ? languages[0] : "Unknown";
        return String.format("Title: %s, Author: %s, Language: %s, Downloads: %d", title, authorNames, language, downloads);
    }
}