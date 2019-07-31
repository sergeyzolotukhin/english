package ua.in.sz.integration;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import ua.in.sz.english.service.parser.book.PageDto;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
@MessageEndpoint
public class BookParserInt {
    @Splitter(inputChannel = "bookChannel", outputChannel = "pageChannel")
    public Iterator<PageDto> parse(File file) throws IOException {
        PdfReader reader = new PdfReader(file.getAbsolutePath());

        int pages = reader.getNumberOfPages();
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        return new Iterator<PageDto>() {
            int page = 1;

            @Override
            public boolean hasNext() {
                return page < pages;
            }

            @Override
            public PageDto next() {
                try {
                    String text = PdfTextExtractor.getTextFromPage(reader, page, strategy);
                    page++;
                    return new PageDto("", page, text);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
