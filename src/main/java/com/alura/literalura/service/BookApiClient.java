package com.alura.literalura.service;

import com.alura.literalura.model.Book;
import com.alura.literalura.model.BookApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class BookApiClient implements CommandLineRunner {
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
                BookApiResponse apiResponse = objectMapper.readValue(response.body(), BookApiResponse.class);
                return apiResponse.getResults();
            } else {
                throw new RuntimeException("Error en la solicitud: Código de estado " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error analizando la respuesta JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public void run(String... args) {
        mostrarMenu();
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n--- Menú de opciones ---");
            System.out.println("1. Consultar libros");
            System.out.println("2. Consultar por autor");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea restante

            switch (opcion) {
                case 1:
                    consultarLibros();
                    break;
                case 2:
                    System.out.print("Ingrese el nombre del autor: ");
                    String autor = scanner.nextLine();
                    consultarPorAutor(autor);
                    break;
                case 3:
                    continuar = false;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
        scanner.close();
    }

    public void consultarLibros() {
        String apiUrl = "https://gutendex.com/books/?page=2";
        List<Book> books = fetchBooks(apiUrl);
        books.forEach(System.out::println);
    }

    public void consultarPorAutor(String autor) {
        String apiUrl = "https://gutendex.com/books/?search=" + autor;
        List<Book> books = fetchBooks(apiUrl);

        if (books.isEmpty()) {
            System.out.println("No se encontraron libros para el autor: " + autor);
        } else {
            books.forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        new BookApiClient().run();
    }
}
