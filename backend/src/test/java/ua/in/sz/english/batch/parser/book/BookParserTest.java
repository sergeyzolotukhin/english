package ua.in.sz.english.batch.parser.book;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Queue;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@ExtendWith(MockitoExtension.class)
class BookParserTest {
    @Mock
    private Queue<Event> queue;

    @Test
    void run() throws InterruptedException {
        new BookParser(queue, "test-book.pdf").run();
        Mockito.verify(queue, Mockito.times(5)).put(Mockito.isA(Event.class));
    }

    @Test
    void layers() {
        ArchRule rule = layeredArchitecture()
                .layer("Web").definedBy("..ua.in.sz.english.web..")
                .layer("Services").definedBy("..ua.in.sz.english.service..")
                .layer("Batch").definedBy("..ua.in.sz.english.batch..")
                .whereLayer("Web").mayNotBeAccessedByAnyLayer()
                .whereLayer("Services").mayOnlyBeAccessedByLayers("Web")
                .whereLayer("Batch").mayOnlyBeAccessedByLayers("Services");

        rule.check(new ClassFileImporter().importPackages("..ua.in.sz.english.."));
    }
}