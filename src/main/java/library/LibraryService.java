package library;

import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    private final LinkedHashMap<Integer, Book> books = new LinkedHashMap<>();
    private int nextId = 1;

    public Book add(String title, String author, int year) {
        boolean duplicate = books.values().stream()
                .anyMatch(book -> book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author));

        if (duplicate) {
            throw new IllegalArgumentException("Книга с таким названием и автором уже существует");
        }

        Book book = new Book(nextId++, title, author, year);
        books.put(book.getId(), book);

        return book;
    }

    public Book remove(int id) {
        Book book = books.remove(id);

        if (book == null) {
            throw new NoSuchElementException("Книга с ID " + id + " не найдена");
        }

        return book;
    }

    public List<Book> list(String sortBy) {
        List<Book> result = new ArrayList<>(books.values());

        switch (sortBy == null ? "" : sortBy.toLowerCase()) {
            case "title" -> result.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
            case "author" -> result.sort(Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER));
            case "year" -> result.sort(Comparator.comparingInt(Book::getYear));
            case "" -> {}
            default -> throw new IllegalArgumentException("Неизвестный аргумент: " + sortBy + ", допустимые: title, author, year");
        }

        return result;
    }

    public List<Book> find(String arg) {
        String query = arg.toLowerCase();

        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public String stats() {
        if (books.isEmpty()) {
            return "В библиотеке нет книг";
        }

        int total = books.size();

        Book oldest = books.values().stream()
                .min(Comparator.comparingInt(Book::getYear)).orElseThrow();
        Book newest = books.values().stream()
                .max(Comparator.comparingInt(Book::getYear)).orElseThrow();

        Map<String, Long> booksByAuthor = books.values().stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));

        String topAuthors = booksByAuthor.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                .limit(3)
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));

        return String.format(
                "Всего книг: %d%n" +
                "Самая старая: %s%n" +
                "Самая новая: %s%n" +
                "Топ 3 авторы: %n%s",
                total, oldest, newest, topAuthors);
    }

    public int size() {
        return books.size();
    }
}
