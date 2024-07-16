package com.example.libreria.service;

import com.example.libreria.model.BooksResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Servicio para gestionar operaciones relacionadas con libros. Este servicio se
 * comunica con la API de Gutendex para realizar búsquedas de libros por título
 * y idioma.
 */
@Service
public class BookService {

    /**
     * La URL base para las solicitudes a la API de Gutendex.
     */
    private final String BASE_URL = "https://gutendex.com/books/";

    /**
     * Realiza una búsqueda de libros por título.
     *
     * @param title el título del libro para buscar. Este título se codifica
     * para incluirlo en la URL de la consulta.
     * @return un objeto {@link BooksResponse} que representa la respuesta de la
     * API que contiene los resultados de la búsqueda.
     * @throws IOException si ocurre un error durante la comunicación con la API
     * o al procesar la respuesta.
     */
    public BooksResponse searchBooksByTitle(String title) throws IOException {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
        String queryUrl = BASE_URL + "?search=" + encodedTitle;

        URL url = new URL(queryUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        // Lee la respuesta
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.toString(), BooksResponse.class);
        } catch (IOException e) {
            // Maneja errores de conexión y respuesta
            e.printStackTrace();
            throw new IOException("Error al leer la respuesta del servidor: " + e.getMessage(), e);
        }
    }

    /**
     * Realiza una búsqueda de libros por idioma.
     *
     * @param language el idioma en el que se desea buscar libros. Este valor se
     * incluye directamente en la URL de la consulta.
     * @return un objeto {@link BooksResponse} que representa la respuesta de la
     * API que contiene los resultados de la búsqueda.
     * @throws IOException si ocurre un error durante la comunicación con la API
     * o al procesar la respuesta.
     */
    public BooksResponse searchBooksByLanguage(String language) throws IOException {
        String queryUrl = BASE_URL + "?languages=" + language;

        URL url = new URL(queryUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Verifica el código de respuesta
        int responseCode = connection.getResponseCode();

        // Lee la respuesta
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.toString(), BooksResponse.class);
        } catch (IOException e) {
            // Maneja errores de conexión y respuesta
            e.printStackTrace();
            throw new IOException("Error al leer la respuesta del servidor: " + e.getMessage(), e);
        }
    }
}
