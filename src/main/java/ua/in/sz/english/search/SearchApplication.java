package ua.in.sz.english.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class SearchApplication {
    private static final String INDEX_PATH = "K:/projects/english/index";

    private static final String FIELD_SENTENCE = "path";

    public static void main(String[] args) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();

        Directory index = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(index, config);

        Document doc = new Document();
        doc.add(new StringField(FIELD_SENTENCE, "This is a critical investment in your business and team.", Field.Store.YES));
        indexWriter.addDocument(doc);

        indexWriter.close();
    }
}
