package ua.in.sz.english.service.parser.book;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class PdfBookParser implements Runnable {
    private final BlockingQueue<PageDto> queue;
    private final String path;

    @Override
    public void run() {
        log.info("Parse book book: {}", path);

        try (PdfPageReader reader = new PdfPageReader(path)) {
            for (int page = 1; page < reader.getNumberOfPages(); page++) {
                String text = PdfTextExtractor.getTextFromPage(reader, page, new SimpleTextExtractionStrategy());

                if (log.isTraceEnabled()) {
                    log.trace("Send page: {}", page);
                }

                queue.put(new PageDto(path, page, text));
            }

            queue.put(new PageDto(path, PageDto.LAST, StringUtils.EMPTY));
        } catch (IOException | InterruptedException e) {
            log.error("Can't parse book", e);
        }
    }

    private static class PdfPageReader extends PdfReader implements AutoCloseable {
        PdfPageReader(String filename) throws IOException {
            super(filename);
        }
    }
}
