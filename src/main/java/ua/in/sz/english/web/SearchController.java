package ua.in.sz.english.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.in.sz.english.service.parser.BookParserService;
import ua.in.sz.english.service.tokenizer.SentenceParserService;

@Slf4j
@RestController
public class SearchController {
    private final BookParserService bookParserService;
    private final SentenceParserService sentenceParserService;

    @Autowired
    public SearchController(
            BookParserService bookParserService,
            SentenceParserService sentenceParserService) {
        this.bookParserService = bookParserService;
        this.sentenceParserService = sentenceParserService;
    }

    @RequestMapping("/")
    public String index() {
        return "Use book - to parse book, sentence - to parse text";
    }

    @RequestMapping("/book")
    public String parseBook() {
        bookParserService.parseBook();
        return "OK";
    }

    @RequestMapping("/sentence")
    public String processSentence() {
        sentenceParserService.processSentence();
        return "OK";
    }
}
