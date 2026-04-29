package library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {
    private LibraryService service;

    @BeforeEach
    void init() {
        service = new LibraryService();
    }

    @Test
    void add_assignIncrementIds() {
        Book book1 = service.add("Мы", "Замятин", 1920);
        Book book2 = service.add("451 градус по Фаренгейту", "Брэдбери", 1953);

        assertEquals(1, book1.getId());
        assertEquals(2, book2.getId());
        assertEquals(2, service.size());
    }

    @Test
    void remove_returnRemovedBook() {
        Book added = service.add("Дюна", "Герберт", 1965);
        Book removed = service.remove(added.getId());

        assertEquals(added.getId(), removed.getId());
        assertEquals(0, service.size());
    }

    @Test
    void list_sortByYear() {
        service.add("Дюна", "Герберт", 1965);
        service.add("Мы", "Замятин", 1920);
        List<Book> books = service.list("year");

        assertEquals(1920, books.get(0).getYear());
        assertEquals(1965, books.get(1).getYear());
    }

    @Test
    void find_partialMatch() {
        service.add("451 градус по Фаренгейту", "Брэдбери", 1953);
        service.add("О дивный новый мир", "Хаксли", 1932);
        List<Book> result = service.find("градус");

        assertEquals(1, result.size());
        assertEquals("451 градус по Фаренгейту", result.get(0).getTitle());
    }
}
