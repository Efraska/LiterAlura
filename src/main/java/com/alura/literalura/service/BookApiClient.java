package com.alura.literalura.service;

import com.alura.literalura.model.Book;
import com.alura.literalura.model.BookApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BookApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BookApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<Book> fetchBooks(String apiUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Deserializar JSON en BookApiResponse
                BookApiResponse apiResponse = objectMapper.readValue(response.body(), BookApiResponse.class);
                return apiResponse.getResults(); // Retornar la lista de libros
            } else {
                throw new RuntimeException("Error en la solicitud: Código de estado " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error analizando la respuesta JSON: " + e.getMessage(), e);
        }
    }


    public static void main(String[] args) {
        String apiUrl = "https://gutendex.com/books/?page=2";
        BookApiClient client = new BookApiClient();
        List<Book> books = client.fetchBooks(apiUrl);

        // Imprimir los libros obtenidos
        books.forEach(book -> {
            System.out.println("Título: " + book.getTitle());
            book.getAuthors().forEach(author -> System.out.println("Autor: " + author.getName()));
        });
    }

}
