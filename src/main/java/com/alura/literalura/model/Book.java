package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora los campos desconocidos del JSON
public class Book {
    private int id;
    private String title;

    @JsonAlias("authors") // Permite mapear un alias si hay otro nombre para esta propiedad
    private List<Author> authors;

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

    // Método toString para mostrar la información del libro
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        private String name;

        // Getters y Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        // Método toString para mostrar la información del autor
        @Override
        public String toString() {
            return "Author{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
