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
            System.out.println("1. Consultar libros");
            System.out.println("2. Consultar por autor");
            System.out.println("3. Lista de autores");
            System.out.println("4. Listar autores vivos en un año");
            System.out.println("5. Salir");
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
                    List<Book> librosParaAutores = fetchBooks("https://gutendex.com/books/?page=2");
                    listarAutores(librosParaAutores);
                    break;
                case 4:
                    System.out.print("Ingrese el año para filtrar autores vivos: ");
                    int year = scanner.nextInt();
                    List<Book> librosParaVivos = fetchBooks("https://gutendex.com/books/?page=2");
                    listarAutoresVivos(librosParaVivos, year);
                    break;
                case 5:
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el título del libro que desea buscar: ");
        String titulo = scanner.nextLine();

        String apiUrl = "https://gutendex.com/books/?search=" + titulo;

        try {
            List<Book> books = fetchBooks(apiUrl);

            if (books.isEmpty()) {
                System.out.println("No se encontraron libros con el título: " + titulo);
            } else {
                System.out.println("\n--- Resultado de la búsqueda ---");
                books.forEach(book -> {
                    System.out.println("Título: " + book.getTitle());
                    System.out.println("Autor: " + book.getAuthors().get(0).getName());
                    System.out.println("Idiomas: " + book.getLanguages());
                    System.out.println("Descargas: " + book.getDownloadCount());
                    System.out.println("------------------------------");
                });
            }
        } catch (Exception e) {
            System.out.println("Error al consultar libros: " + e.getMessage());
        }
    }
    public void consultarPorAutor(String autor) {
        String apiUrl = "https://gutendex.com/books/?search=" + autor;

        try {
            List<Book> books = fetchBooks(apiUrl);

            if (books.isEmpty()) {
                System.out.println("No se encontraron libros para el autor: " + autor);
            } else {
                System.out.println("\n--- Libros encontrados para el autor " + autor + " ---");
                books.forEach(book -> {
                    System.out.println("Título: " + book.getTitle());
                    System.out.println("Autor: " + book.getAuthors().get(0).getName());
                    System.out.println("Idiomas: " + book.getLanguages());
                    System.out.println("Descargas: " + book.getDownloadCount());
                    System.out.println("------------------------------");
                });
            }
        } catch (Exception e) {
            System.out.println("Error al consultar por autor: " + e.getMessage());
        }
    }


    public void listarAutores(List<Book> books) {
        System.out.println("\n--- Lista de Autores ---");
        books.stream()
                .map(Book::getAuthors)
                .filter(authors -> authors != null && !authors.isEmpty())
                .map(authors -> authors.get(0)) // Tomar el primer autor
                .forEach(author -> System.out.println(author.getName()));
    }

    public void listarAutoresVivos(List<Book> books, int year) {
        System.out.println("\n--- Autores vivos en el año " + year + " ---");
        books.stream()
                .map(Book::getAuthors)
                .filter(authors -> authors != null && !authors.isEmpty())
                .map(authors -> authors.get(0)) // Tomar el primer autor
                .filter(author -> author.getBirthYear() != null && author.getBirthYear() <= year &&
                        (author.getDeathYear() == null || author.getDeathYear() > year)) // Filtrar autores vivos
                .forEach(author -> System.out.println(author.getName() + " (n. " + author.getBirthYear() + ")"));
    }


    public static void main(String[] args) {
        new BookApiClient().run();
    }
}
