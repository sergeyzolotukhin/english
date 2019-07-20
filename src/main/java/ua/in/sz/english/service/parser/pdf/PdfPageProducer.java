package ua.in.sz.english.service.parser.pdf;

import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class PdfPageProducer implements Runnable {
    private final String path;
    private final BlockingQueue<PdfPageDto> queue;

    @Override
    public void run() {
        log.info("Parse pdf book: {}", path);

        try (PdfPageReader reader = new PdfPageReader(path)) {
            for (Integer page : PdfPageRange.of(9, 119)) {
                String text = PdfTextExtractor.getTextFromPage(reader, page, new SimpleTextExtractionStrategy());

                if (log.isTraceEnabled()) {
                    log.trace("Send page: {}", page);
                }

                queue.put(new PdfPageDto(path, page, text));
            }

            queue.put(new PdfPageDto(path, PdfPageDto.LAST, StringUtils.EMPTY));
        } catch (IOException | InterruptedException e) {
            log.error("Can't parse book", e);
        }
    }
}
