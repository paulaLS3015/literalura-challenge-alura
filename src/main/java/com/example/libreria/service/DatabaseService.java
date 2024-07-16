package com.example.libreria.service;

import com.example.libreria.model.Author;
import com.example.libreria.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para realizar operaciones de guardado en la base de datos
 * relacionadas con libros y autores. Utiliza {@link JdbcTemplate} para
 * interactuar con la base de datos.
 */
@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Guarda un libro en la base de datos.
     *
     * @param book el libro a guardar. Se inserta en la tabla 'books' con su
     * título, número de descargas, idiomas y autores.
     */
    public void saveBookToDatabase(Book book) {
        String sql = "INSERT INTO books (title, downloads, languages, authors) VALUES (?, ?, ?, ?)";
        try {
            String languagesString = convertArrayToString(book.getLanguages());
            String authorsString = convertAuthorsToString(book.getAuthors());

            jdbcTemplate.update(sql, book.getTitle(), book.getDownloads(), languagesString, authorsString);
            System.out.println("Libro guardado en la base de datos.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar el libro en la base de datos.");
        }
    }

    /**
     * Guarda una lista de autores en la base de datos.
     *
     * @param authors la lista de autores a guardar. Cada autor se inserta en la
     * tabla 'authors' con su nombre, año de nacimiento y año de fallecimiento.
     */
    public void saveAuthorsToDatabase(List<Author> authors) {
        String sql = "INSERT INTO authors (name, birth_year, death_year) VALUES (?, ?, ?)";
        try {
            for (Author author : authors) {
                jdbcTemplate.update(sql, author.getName(), author.getBirthYear(), author.getDeathYear());
            }
            System.out.println("Autores guardados en la base de datos.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar los autores en la base de datos.");
        }
    }

    /**
     * Convierte un array de cadenas en una cadena delimitada por comas.
     *
     * @param array el array de cadenas a convertir.
     * @return una cadena que contiene todos los elementos del array, separados
     * por comas.
     */
    private String convertArrayToString(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        return Arrays.stream(array)
                .collect(Collectors.joining(", "));
    }

    /**
     * Convierte un array de autores en una cadena delimitada por comas.
     *
     * @param authors el array de autores a convertir.
     * @return una cadena que contiene los nombres de todos los autores,
     * separados por comas.
     */
    private String convertAuthorsToString(Author[] authors) {
        if (authors == null || authors.length == 0) {
            return "";
        }
        return Arrays.stream(authors)
                .map(Author::getName)
                .collect(Collectors.joining(", "));
    }
}
