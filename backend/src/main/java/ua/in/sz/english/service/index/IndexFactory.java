package ua.in.sz.english.service.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public final class IndexFactory {
    private IndexFactory() {
        // no instance
    }

    public static FSDirectory createDirectory(String path) throws IOException {
        return FSDirectory.open(Paths.get(path));
    }

    public static StandardAnalyzer createAnalyzer() {
        return new StandardAnalyzer();
    }

    public static IndexWriter createIndexWriter(Analyzer analyzer, Directory directory) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(directory, config);
    }

    static IndexSearcher createSearcher(IndexReader reader) {
        return new IndexSearcher(reader);
    }

    static DirectoryReader createReader(Directory index) throws IOException {
        return DirectoryReader.open(index);
    }
}
