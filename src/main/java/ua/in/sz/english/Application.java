package ua.in.sz.english;

import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

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
}
