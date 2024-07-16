// StatisticsService.java
package com.example.libreria.service;

import com.example.libreria.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para obtener estadísticas relacionadas con los libros en la base de
 * datos. Utiliza {@link JdbcTemplate} para realizar consultas a la base de
 * datos.
 */
@Service
public class StatisticsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Obtiene el número de libros disponibles para cada idioma especificado.
     *
     * @param languages la lista de idiomas para los cuales se desea obtener el
     * conteo de libros.
     * @return un mapa donde la clave es el idioma y el valor es el número de
     * libros disponibles en ese idioma.
     * @throws IllegalArgumentException si la lista de idiomas está vacía o es
     * nula.
     */
    public Map<String, Integer> getBookCountByLanguage(List<String> languages) {
        String sql = "SELECT language, COUNT(*) as count FROM books WHERE language IN (?) GROUP BY language";
        String languagesString = String.join(", ", languages);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, languagesString);
        return rows.stream()
                .collect(Collectors.toMap(
                        row -> (String) row.get("language"),
                        row -> ((Number) row.get("count")).intValue()
                ));
    }
}
