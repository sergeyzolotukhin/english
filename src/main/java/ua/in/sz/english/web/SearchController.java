package ua.in.sz.english.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.in.sz.english.service.parser.BookParserService;
import ua.in.sz.english.service.search.SearchService;

@Slf4j
@RestController
public class SearchController {
    private final BookParserService bookParserService;
    private final SearchService searchService;

    @Autowired
    public SearchController(BookParserService bookParserService,
                            SearchService searchService) {
        this.bookParserService = bookParserService;
        this.searchService = searchService;
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
        bookParserService.parseText();
        return "OK";
    }

    @RequestMapping("/index")
    public String indexing() {
        searchService.indexing();
        return "OK";
    }

    @RequestMapping("/search")
    public String search(@RequestParam("q") String query) {
        return String.join("<br/>", searchService.search(query));
    }
}
