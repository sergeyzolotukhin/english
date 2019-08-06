package ua.in.sz.english.batch.parser.book;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Events;
import ua.in.sz.english.batch.queue.Queue;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class BookParser implements Runnable {
    private final Queue<Event> queue;
    private final String path;

    @Override
    public void run() {
        try (PdfPageReader reader = new PdfPageReader(path)) {
            log.debug("Start parse book: {}", path);

            queue.put(Events.START_EVENT);

            doParse(reader);

            queue.put(Events.END_EVENT);

            log.debug("End parse book: {}", path);
        } catch (IOException | InterruptedException e) {
            log.error("Can't parse book", e);
        }
    }

    private void doParse(PdfPageReader reader) throws IOException, InterruptedException {
        for (int page = 1; page < reader.getNumberOfPages(); page++) {
            String text = PdfTextExtractor.getTextFromPage(reader, page, new SimpleTextExtractionStrategy());

            if (log.isTraceEnabled()) {
                log.trace("Send page: {}", page);
            }

            queue.put(new PageDto(path, page, text));
        }
    }

    private static class PdfPageReader extends PdfReader implements AutoCloseable {
        PdfPageReader(String filename) throws IOException {
            super(filename);
        }
    }
}
