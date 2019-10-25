package ua.in.sz.english.service.dictionary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ua.in.sz.english.store.WordRepository;
import ua.in.sz.english.store.entity.Word;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "english", name = "wordStoreType", havingValue = "mongo")
public class MongoDictionaryService implements DictionaryService {

	private final WordRepository wordRepository;

	public MongoDictionaryService(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	@Override
	public String definition(String word) {
		return wordRepository.findById(word)
				.map(Word::getDescription)
				.orElseThrow(WordNotFoundException::new);
	}
}
