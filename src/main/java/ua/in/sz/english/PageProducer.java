package ua.in.sz.english;

import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class PageProducer implements Runnable {
    private final String path;
    private final BlockingQueue<PageDto> queue;

    @Override
    public void run() {
        log.info("Parse book: {}", path);

        try (PdfReaderClosable reader = new PdfReaderClosable(path)) {
            for (Integer page : PageRange.of(9, 119)) {
                String text = PdfTextExtractor.getTextFromPage(reader, page, new SimpleTextExtractionStrategy());

                if (log.isTraceEnabled()) {
                    log.trace("Send page: {}", page);
                }

                queue.put(new PageDto(path, page, text));
            }

            queue.put(new PageDto(path, PageDto.LAST_PAGE, StringUtils.EMPTY));
        } catch (IOException | InterruptedException e) {
            log.error("Can't parse book", e);
        }
    }
}
