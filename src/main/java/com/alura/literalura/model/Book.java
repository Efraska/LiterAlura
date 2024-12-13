package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private int id;
    private String title;

    @JsonProperty("authors")
    private List<Author> authors;

    @JsonProperty("languages")
    private List<String> languages;

    @JsonProperty("download_count")
    private int downloadCount;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        String authorNames = authors.stream()
                .map(Author::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Desconocido");

        return "Título: " + title + "\n" +
                "Autor: " + authorNames + "\n" +
                "Idioma: " + (languages.isEmpty() ? "Desconocido" : languages.get(0)) + "\n" +
                "Descargas: " + downloadCount;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        private String name;

        @JsonProperty("birth_year")
        private Integer birthYear;

        @JsonProperty("death_year")
        private Integer deathYear;

        // Getters y Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(Integer birthYear) {
            this.birthYear = birthYear;
        }

        public Integer getDeathYear() {
            return deathYear;
        }

        public void setDeathYear(Integer deathYear) {
            this.deathYear = deathYear;
        }

        @Override
        public String toString() {
            return "Autor: " + name + ", Año de nacimiento: " + birthYear + ", Año de fallecimiento: " + (deathYear != null ? deathYear : "Vivo");
        }
    }
}
