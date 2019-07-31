package ua.in.sz.english.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;
import ua.in.sz.english.AppProperties;
import ua.in.sz.english.service.index.IndexConstant;
import ua.in.sz.english.service.index.IndexFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    private final AppProperties appProperties;

    public SearchService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public List<String> search(String queryString, int limit) {
        try (
                Directory directory = IndexFactory.createDirectory(appProperties.getIndexDirPath());
                IndexReader reader = IndexFactory.createReader(directory);
                StandardAnalyzer analyzer = IndexFactory.createAnalyzer();
        ) {
            List<String> result = new ArrayList<>();

            IndexSearcher searcher = IndexFactory.createSearcher(reader);
            Query query = new QueryParser(IndexConstant.FIELD_SENTENCE, analyzer).parse(queryString);

            TopDocs search = searcher.search(query, limit);

            for (ScoreDoc doc : search.scoreDocs) {
                Document document = searcher.doc(doc.doc);
                String sentence = document.get(IndexConstant.FIELD_SENTENCE);
                result.add(sentence);
            }

            return result;
        } catch (IOException | ParseException e) {
            log.error("Can't build", e);
            return Collections.emptyList();
        }
    }
}
