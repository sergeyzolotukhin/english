package ua.in.sz.english.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.in.sz.english.service.AdminService;
import ua.in.sz.english.service.SearchService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {
    private final SearchService searchService;
    private final AdminService adminService;

    @Autowired
    public MainController(SearchService searchService, AdminService adminService) {
        this.searchService = searchService;
        this.adminService = adminService;
    }

    @RequestMapping("/search")
    public List<String> search(@RequestParam("query") String query,
                               @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        return searchService.search(query, limit);
    }

    @RequestMapping("/index/book")
    public void indexBook() {
        adminService.indexBook();
    }
}
