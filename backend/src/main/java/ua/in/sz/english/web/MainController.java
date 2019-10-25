package ua.in.sz.english.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.in.sz.english.service.AdminService;
import ua.in.sz.english.service.SearchService;
import ua.in.sz.english.service.dictionary.DictionaryService;
import ua.in.sz.english.service.dictionary.WordNotFoundException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class MainController {
	private final AdminService adminService;

	private final SearchService searchService;
	private final DictionaryService dictionaryService;

	@Autowired
	public MainController(
			AdminService adminService,
			SearchService searchService, DictionaryService dictionaryService) {

		this.searchService = searchService;
		this.adminService = adminService;
		this.dictionaryService = dictionaryService;
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

	@RequestMapping(value = "/definition")
	public String definition(@RequestParam("word") String word) {
		try {
			return dictionaryService.definition(word);
		} catch (WordNotFoundException e) {
			return String.format("Word not found [%s]", word);
		}
	}
}
