package com.alura.literalura.service;

import com.alura.literalura.model.Book;
import com.alura.literalura.model.BookApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookApiClient implements CommandLineRunner {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final List<Book> searchedBooks;

    public BookApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        this.objectMapper = new ObjectMapper();
        this.searchedBooks = new ArrayList<>();
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

    public void buscarLibroPorTitulo(String titulo) {
        String apiUrl = "https://gutendex.com/books/?search=" + titulo;
        List<Book> books = fetchBooks(apiUrl);

        if (!books.isEmpty()) {
            Book book = books.get(0); // Tomar el primer libro encontrado
            searchedBooks.add(book);
            System.out.println("Libro encontrado:");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor(es): " + book.getAuthors().stream().map(Book.Author::getName).toList());
            System.out.println("Idiomas: " + book.getLanguages());
            System.out.println("Número de descargas: " + book.getDownloadCount());
        } else {
            System.out.println("No se encontró ningún libro con el título: " + titulo);
        }
    }

    public void listarLibros() {
        if (searchedBooks.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Listado de libros registrados:");
            searchedBooks.forEach(book -> {
                System.out.println("Título: " + book.getTitle());
                System.out.println("Autor(es): " + book.getAuthors().stream().map(Book.Author::getName).toList());
                System.out.println("Idiomas: " + book.getLanguages());
                System.out.println("Número de descargas: " + book.getDownloadCount());
                System.out.println("-------------------------");
            });
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
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar libros registrados");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la línea restante

            switch (opcion) {
                case 1 -> {
                    System.out.print("Ingrese el título del libro: ");
                    String titulo = scanner.nextLine();
                    buscarLibroPorTitulo(titulo);
                }
                case 2 -> listarLibros();
                case 3 -> {
                    continuar = false;
                    System.out.println("Saliendo del programa...");
                }
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new BookApiClient().run();
    }
}
