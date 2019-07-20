package ua.in.sz.english.service.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
public class SearchApplication {
    private static final String INDEX_PATH = "K:/projects/english/index";

    private static final String FIELD_SENTENCE = "path";

    public static void main(String[] args) throws IOException, ParseException {
//        createIndex();
        search("and");
    }

    private static void search(String query) throws IOException, ParseException {
        Directory index = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Query q = new QueryParser(FIELD_SENTENCE, analyzer).parse(query);

        TopDocs search = searcher.search(q, 20);
        ScoreDoc[] scoreDocs = search.scoreDocs;

        for (ScoreDoc doc : scoreDocs) {
            Document document = searcher.doc(doc.doc);
            log.info("Search result: {}", document.get(FIELD_SENTENCE));
        }
    }

    private static void createIndex() throws IOException {
        Analyzer analyzer = new StandardAnalyzer();

        Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter index = new IndexWriter(directory, config);

        addDocument(index, "The goal should be not only to learn and refine but to learn and refine as quickly as possible.");
        addDocument(index, "This is a critical investment in your business and team.");

        index.close();
    }

    private static void addDocument(IndexWriter index, String sentence) throws IOException {
        Document doc = new Document();
        doc.add(new TextField(FIELD_SENTENCE, sentence, Field.Store.YES));
        index.addDocument(doc);
    }
}
