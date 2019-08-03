package ua.in.sz.english.integration.parser;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import ua.in.sz.english.service.parser.book.PageDto;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
@MessageEndpoint
public class BookParserEndpoint {
    @Splitter(inputChannel = "bookChannel", outputChannel = "pageChannel")
    public Iterator<PageDto> parse(File file) throws IOException {
        PdfReader reader = new PdfReader(file.getAbsolutePath());
        return new PageIt(reader);
    }

    private static class PageIt implements Iterator<PageDto> {
        private final int pages;
        private final PdfReader reader;
        private final TextExtractionStrategy strategy;

        private int page = 1;

        PageIt(PdfReader reader) {
            this.reader = reader;
            this.pages = reader.getNumberOfPages();
            this.strategy = new SimpleTextExtractionStrategy();
        }

        @Override
        public boolean hasNext() {
            return page < pages;
        }

        @Override
        public PageDto next() {
            return new PageDto("", page++, pageText());
        }

        private String pageText() {
            try {
                return PdfTextExtractor.getTextFromPage(reader, page, strategy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
