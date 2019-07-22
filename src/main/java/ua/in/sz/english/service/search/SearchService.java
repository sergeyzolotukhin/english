package ua.in.sz.english.service.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.search.index.IndexDto;
import ua.in.sz.english.service.search.index.IndexUtil;
import ua.in.sz.english.service.search.index.SentenceIndexWriter;
import ua.in.sz.english.service.search.index.SentenceReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class SearchService {

    @Value("${sentence.path}")
    private String sentencePath;
    @Value("${index.path}")
    private String indexPath;

    private final TaskExecutor parserTaskExecutor;

    @Autowired
    public SearchService(TaskExecutor parserTaskExecutor) {
        this.parserTaskExecutor = parserTaskExecutor;
    }

    public void indexing() {
        BlockingQueue<IndexDto> queue = new ArrayBlockingQueue<>(100);
        SentenceReader bookParser = new SentenceReader(queue, sentencePath);
        SentenceIndexWriter textWriter = new SentenceIndexWriter(queue, indexPath);

        parserTaskExecutor.execute(bookParser);
        parserTaskExecutor.execute(textWriter);
    }

    public List<String> search(String query) {
        try (
                Directory directory = IndexUtil.createDirectory(indexPath);
                IndexReader reader = createReader(directory);
                StandardAnalyzer analyzer = IndexUtil.createAnalyzer();
        ) {
            List<String> result = new ArrayList<>();

            IndexSearcher searcher = createSearcher(reader);
            Query q = new QueryParser(IndexUtil.FIELD_SENTENCE, analyzer).parse(query);

            TopDocs search = searcher.search(q, 20);

            for (ScoreDoc doc : search.scoreDocs) {
                Document document = searcher.doc(doc.doc);
                String sentence = document.get(IndexUtil.FIELD_SENTENCE);
                result.add(sentence);
            }

            return result;
        } catch (IOException | ParseException e) {
            log.error("Can't search", e);
            return Collections.emptyList();
        }
    }

    private IndexSearcher createSearcher(IndexReader reader) {
        return new IndexSearcher(reader);
    }

    private DirectoryReader createReader(Directory index) throws IOException {
        return DirectoryReader.open(index);
    }
}
