package library;

import java.util.List;

public class CommandParser {
    private final LibraryService service;

    public CommandParser(LibraryService service) {
        this.service = service;
    }

    public String handle(String line) {
        if (line == null || line.isBlank()) {
            return "Ошибка, введена пустая команда";
        }

        String[] parts = line.strip().split(" ", 2);
        String cmd = parts[0].toUpperCase();
        String args = parts.length > 1 ? parts[1].strip() : "";

        return switch (cmd) {
            case "ADD" -> handleAdd(args);
            case "REMOVE" -> handleRemove(args);
            case "LIST" -> handleList(args);
            case "FIND" -> handleFind(args);
            case "STATS" -> handleStats();
            case "EXIT" -> null;
            default -> "Ошибка, неизвестная команда: " + cmd;
        };
    }

    private String handleAdd(String args) {
        String[] f = args.split(";", -1);

        if (f.length != 3 || f[0].isBlank() || f[1].isBlank() || f[2].isBlank()) {
            return "Ошибка формата, введите: ADD <title>;<author>;<year>";
        }

        String title = f[0].strip();
        String author = f[1].strip();

        int year;
        try {
            year = Integer.parseInt(f[2].strip());
        } catch (NumberFormatException e) {
            return "Ошибка, год должен быть числом";
        }

        try {
            Book book = service.add(title, author, year);
            return "Добавлена книга: " + book;
        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    private String handleRemove(String args) {
        if (args.isBlank()) {
            return "Ошибка формата, используйте: REMOVE <id>";
        }

        int id;
        try {
            id = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return "Ошибка, ID должен быть числом";
        }

        try {
            Book book = service.remove(id);
            return "Удалена книга: " + book;
        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    private String handleList(String args) {
        try {
            List<Book> books = service.list(args.isBlank() ? null : args);

            if (books.isEmpty()) {
                return "В библиотеке нет книг";
            }

            StringBuilder sb = new StringBuilder("Список книг:\n");
            books.forEach(b -> sb.append(b).append("\n"));

            return sb.toString().stripTrailing();
        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    private String handleFind(String args) {
        if (args.isBlank()) {
            return "Ошибка формата, используйте: FIND <query>";
        }

        List<Book> found = service.find(args);
        if (found.isEmpty()) {
            return "Ничего не найдено по запросу: " + args;
        }

        StringBuilder sb = new StringBuilder("Найдено " + found.size() + " книг:\n");
        found.forEach(b -> sb.append(b).append("\n"));

        return sb.toString().stripTrailing();
    }

    private String handleStats() {
        return service.stats();
    }
}
