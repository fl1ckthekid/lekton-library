package library;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        CommandParser parser = new CommandParser(service);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Консольное приложение \"Библиотека\"");
        System.out.println("Доступные команды: ADD <title>;<author>;<year>, REMOVE <id>, LIST <title|author|year>, FIND <query>, STATS, EXIT");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String response = parser.handle(line);
            if (response == null) {
                System.out.println("Завершение работы...");
                break;
            }
            System.out.println(response);
        }
    }
}
