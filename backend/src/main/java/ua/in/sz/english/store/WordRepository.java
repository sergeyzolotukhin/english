package ua.in.sz.english.store;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ua.in.sz.english.store.entity.Word;

@Repository
public interface WordRepository extends MongoRepository<Word, String> {
	
}
