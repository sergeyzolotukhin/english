package ua.in.sz.english.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.in.sz.english.service.index.SentenceIndexService;
import ua.in.sz.english.service.parser.BookParserService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {
    private final BookParserService bookParserService;
    private final SentenceIndexService sentenceIndexService;

    @Autowired
    public MainController(BookParserService bookParserService,
                          SentenceIndexService sentenceIndexService) {
        this.bookParserService = bookParserService;
        this.sentenceIndexService = sentenceIndexService;
    }

    @GetMapping(value = "/simple")
    public List<String> index() {
        return Arrays.asList("book", "sentence", "index", "search");
    }

    @GetMapping(value = "/session")
    public String session() {
        return "0123456";
    }

    @RequestMapping("/book")
    public void parseBook() {
        bookParserService.parseBook();
    }

    @RequestMapping("/sentence")
    public void processSentence() {
        bookParserService.parseText();
    }

    @RequestMapping("/index")
    public void indexing() {
        sentenceIndexService.indexing();
    }

    @RequestMapping("/search")
    public String search(@RequestParam("q") String query,
                         @RequestParam(value = "l", required = false, defaultValue = "20") int limit) {
        return String.join("<br/>", sentenceIndexService.search(query, limit));
    }
}
