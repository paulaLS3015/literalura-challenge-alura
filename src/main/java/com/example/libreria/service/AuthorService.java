package com.example.libreria.service;

import com.example.libreria.model.Author;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con autores. Este servicio
 * se comunica con la API de Gutendex para obtener información sobre autores.
 */
@Service
public class AuthorService {

    /**
     * La URL base para las solicitudes a la API de Gutendex.
     */
    private final String BASE_URL = "https://gutendex.com/books";

    /**
     * Obtiene una lista de todos los autores disponibles en la API de Gutendex.
     *
     * @return una lista de objetos {@link Author} que representan autores
     * únicos.
     * @throws IOException si ocurre un error durante la comunicación con la API
     * o al procesar la respuesta.
     */
    public List<Author> listAllAuthors() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        System.out.println("Request URL: " + url);

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseMap = mapper.readValue(response.toString(), Map.class);
                List<Map<String, Object>> books = (List<Map<String, Object>>) responseMap.get("results");

                Set<Author> authors = new HashSet<>();
                for (Map<String, Object> book : books) {
                    List<Map<String, Object>> bookAuthors = (List<Map<String, Object>>) book.get("authors");
                    if (bookAuthors != null) {
                        for (Map<String, Object> authorData : bookAuthors) {
                            Author author = new Author();
                            author.setName((String) authorData.get("name"));
                            // Set birth and death year if available
                            author.setBirthYear(parseYear(authorData.get("birth_year")));
                            author.setDeathYear(parseYear(authorData.get("death_year")));
                            authors.add(author);
                        }
                    }
                }

                // Eliminar autores duplicados por nombre
                List<Author> uniqueAuthors = authors.stream()
                        .collect(Collectors.toMap(
                                Author::getName,
                                author -> author,
                                (existing, replacement) -> existing
                        ))
                        .values()
                        .stream()
                        .collect(Collectors.toList());

                return uniqueAuthors;
            } catch (IOException e) {
                throw new IOException("Error reading response from server", e);
            }
        } else {
            throw new IOException("Failed to get data from server. HTTP Response Code: " + responseCode);
        }
    }

    /**
     * Analiza el objeto de año y lo convierte en un entero si es posible.
     *
     * @param yearObject el objeto que contiene el año como una cadena o entero.
     * @return el año como un entero, o null si el objeto no se puede convertir
     * en un año válido.
     */
    private Integer parseYear(Object yearObject) {
        if (yearObject instanceof String) {
            try {
                return Integer.parseInt((String) yearObject);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (yearObject instanceof Integer) {
            return (Integer) yearObject;
        } else {
            return null;
        }
    }

    /**
     * Obtiene una lista de autores que estaban vivos en un año específico.
     *
     * @param year el año en el que se busca la vigencia de los autores.
     * @return una lista de objetos {@link Author} que representan autores
     * únicos vivos en el año especificado.
     * @throws IOException si ocurre un error durante la comunicación con la API
     * o al procesar la respuesta.
     */
    public List<Author> listLivingAuthorsInYear(int year) throws IOException {
        URL url = new URL(BASE_URL + "?author_year_start=" + year);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        System.out.println("Request URL: " + url);

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseMap = mapper.readValue(response.toString(), Map.class);
                List<Map<String, Object>> books = (List<Map<String, Object>>) responseMap.get("results");

                Set<Author> authors = new HashSet<>();
                for (Map<String, Object> book : books) {
                    List<Map<String, Object>> bookAuthors = (List<Map<String, Object>>) book.get("authors");
                    if (bookAuthors != null) {
                        for (Map<String, Object> authorData : bookAuthors) {
                            Author author = new Author();
                            author.setName((String) authorData.get("name"));
                            author.setBirthYear(parseYear(authorData.get("birth_year")));
                            author.setDeathYear(parseYear(authorData.get("death_year")));
                            authors.add(author);
                        }
                    }
                }
                // Eliminar autores duplicados por nombre

                List<Author> uniqueAuthors = authors.stream()
                        .collect(Collectors.toMap(
                                Author::getName,
                                author -> author,
                                (existing, replacement) -> existing
                        ))
                        .values()
                        .stream()
                        .collect(Collectors.toList());

                return uniqueAuthors;
            } catch (IOException e) {
                throw new IOException("Error reading response from server", e);
            }
        } else {
            throw new IOException("Failed to get data from server. HTTP Response Code: " + responseCode);
        }
    }
}
