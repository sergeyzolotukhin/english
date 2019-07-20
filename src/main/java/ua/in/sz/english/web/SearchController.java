package ua.in.sz.english.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.in.sz.english.service.parser.BookParserService;

@Slf4j
@RestController
public class SearchController {
    private final BookParserService bookParserService;

    @Autowired
    public SearchController(BookParserService bookParserService) {
        this.bookParserService = bookParserService;
    }

    @RequestMapping("/")
    public String search() {
        bookParserService.parseBook();
        return "Parsed";
    }
}
