package ua.in.sz.english.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.in.sz.english.service.AdminService;
import ua.in.sz.english.service.index.SentenceIndexService;
import ua.in.sz.english.service.parser.BookParserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {
    private final BookParserService bookParserService;
    private final SentenceIndexService sentenceIndexService;
    private final AdminService adminService;

    @Autowired
    public MainController(BookParserService bookParserService,
                          SentenceIndexService sentenceIndexService,
                          AdminService adminService) {
        this.bookParserService = bookParserService;
        this.sentenceIndexService = sentenceIndexService;
        this.adminService = adminService;
    }

    @RequestMapping("/search")
    public List<String> search(@RequestParam("query") String query,
                               @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        return sentenceIndexService.search(query, limit);
    }

    @RequestMapping("/parse/book")
    public void parseBook() {
        bookParserService.parseBook();
    }

    @RequestMapping("/parse/text")
    public void parseText() {
        bookParserService.parseText();
    }

    @RequestMapping("/build/index")
    public void buildIndex() {
        sentenceIndexService.indexing();
    }

    @RequestMapping("/index/book")
    public void indexBook() {
        adminService.indexBook();
    }
}
