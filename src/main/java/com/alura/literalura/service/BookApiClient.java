package com.alura.literalura.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class BookApiClient {

    private final HttpClient httpClient;

    // Constructor para inicializar HttpClient
    public BookApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

    // Método para realizar solicitudes GET
    public String fetchBooks(String apiUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        System.out.println("Enviando solicitud a: " + apiUrl);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Código de estado HTTP: " + response.statusCode());
            System.out.println("Cuerpo de la respuesta: " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching books from API: " + e.getMessage(), e);
        }
    }

    // Método main para pruebas
    public static void main(String[] args) {
        // URL de prueba
        String apiUrl = "https://gutendex.com/books/?page=2";

        // Instancia del cliente y ejecución de la solicitud
        BookApiClient client = new BookApiClient();
        String response = client.fetchBooks(apiUrl);

        // Imprimir respuesta en consola
        System.out.println(response);
    }
}
