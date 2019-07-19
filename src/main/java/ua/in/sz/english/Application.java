package ua.in.sz.english;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;

import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class Application {
    private static final String PATH =
//            "e:/share/_spark/getting_started_with_apache_spark.pdf"
            "e:/_book/_development/_book/domain-driven-design-distilled.pdf";

    public static void main(String[] args) {
        try (PdfReaderClosable reader = new PdfReaderClosable(PATH)) {
            PageRange pages = new PageRange(9, 119);

            for (Integer page : pages) {
                String text = PdfTextExtractor.getTextFromPage(reader, page, new SimpleTextExtractionStrategy());

                log.info(" Page:{}", page);

                log.info(StringUtils.repeat('=', 80));
                log.info("{}", text);
                log.info(StringUtils.repeat('=', 80));
            }
        } catch (IOException e) {
            log.error("Can't parse book", e);
        }
    }

    private static class PdfReaderClosable extends PdfReader implements AutoCloseable {
        PdfReaderClosable(String filename) throws IOException {
            super(filename);
        }
    }

    private static class PageRange implements Iterable<Integer> {
        final int from;
        final int to;

        PageRange(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                int current = from;

                @Override
                public boolean hasNext() {
                    return current <= to;
                }

                @Override
                public Integer next() {
                    return current++;
                }
            };
        }
    }
}
