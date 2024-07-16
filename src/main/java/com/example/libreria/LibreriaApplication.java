package com.example.libreria;

import com.example.libreria.model.Author;
import com.example.libreria.model.Book;
import com.example.libreria.service.AuthorService;
import com.example.libreria.service.BookService;
import com.example.libreria.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LibreriaApplication implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private DatabaseService databaseService;

    private List<Book> bookList;

    public static void main(String[] args) {
        SpringApplication.run(LibreriaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        bookList = new ArrayList<>();

        while (true) {
            System.out.println("=== Biblioteca ===");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar todos los libros");
            System.out.println("3. Listar todos los autores");
            System.out.println("4. Listar autores vivos en un año");
            System.out.println("5. Buscar libros por idioma");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Ingrese el título del libro: ");
                    String title = scanner.nextLine();
                    searchBooksByTitle(title);
                    break;
                case 2:
                    listBooks();
                    break;
                case 3:
                    listAuthors();
                    break;
                case 4:
                    System.out.print("Ingrese el año para consultar autores vivos: ");
                    int year = scanner.nextInt();
                    listLivingAuthors(year);
                    break;
                case 5:
                    System.out.print("Ingrese el idioma (en/fr): ");
                    String language = scanner.nextLine();
                    searchBooksByLanguage(language);
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void searchBooksByLanguage(String language) {
        try {
            var response = bookService.searchBooksByLanguage(language);
            if (response != null && response.getResults() != null && response.getResults().length > 0) {
                System.out.println("Libros encontrados en el idioma " + language + ":");
                for (Book book : response.getResults()) {
                    System.out.println(book);
                }
            } else {
                System.out.println("No se encontraron libros en el idioma " + language + ".");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al buscar libros por idioma.");
        }
    }

    private void searchBooksByTitle(String title) {
        try {
            var response = bookService.searchBooksByTitle(title);
            if (response != null && response.getResults() != null && response.getResults().length > 0) {
                Book book = response.getResults()[0];
                bookList.add(book);
                System.out.println("Libro encontrado: " + book);

                databaseService.saveBookToDatabase(book);

            } else {
                System.out.println("No se encontró ningún libro con el título proporcionado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al buscar el libro.");
        }
    }

    private void listBooks() {
        if (bookList.isEmpty()) {
            System.out.println("No hay libros en la lista.");
        } else {
            System.out.println("Listado de libros:");
            for (Book book : bookList) {
                System.out.println(book);
            }
        }
    }

    private void listAuthors() {
        try {
            List<Author> authors = authorService.listAllAuthors();
            if (authors.isEmpty()) {
                System.out.println("No hay autores en la lista.");
            } else {
                System.out.println("Listado de autores:");
                for (Author author : authors) {
                    System.out.println("Author: " + author.getName() + ", Birth Year: " + author.getBirthYear() + ", Death Year: " + author.getDeathYear());
                }
                databaseService.saveAuthorsToDatabase(authors);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al listar autores.");
        }
    }

    private void listLivingAuthors(int year) {
        try {
            List<Author> authors = authorService.listLivingAuthorsInYear(year);
            if (authors.isEmpty()) {
                System.out.println("No hay autores vivos en el año " + year + ".");
            } else {
                System.out.println("Listado de autores vivos en el año " + year + ":");
                for (Author author : authors) {
                    System.out.println("Author: " + author.getName() + ", Birth Year: " + author.getBirthYear() + ", Death Year: " + author.getDeathYear());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al listar autores vivos.");
        }
    }
}
