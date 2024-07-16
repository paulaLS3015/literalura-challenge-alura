package com.example.libreria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    public Integer getDeathYear() { return deathYear; }
    public void setDeathYear(Integer deathYear) { this.deathYear = deathYear; }

    @Override
    public String toString() {
        return String.format("Name: %s, Birth Year: %d, Death Year: %d",
            name,
            (birthYear != null ? birthYear : 0),
            (deathYear != null ? deathYear : 0));
    }
}
