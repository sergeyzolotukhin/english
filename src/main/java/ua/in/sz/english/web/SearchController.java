package ua.in.sz.english.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    @RequestMapping("/")
    public String search() {
        return "Greetings from Spring Boot!";
    }
}
