package ua.in.sz.english.batch.index;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    public static IndexWriter createIndexWriter(String directory) {
        try {
            IndexWriterConfig config = new IndexWriterConfig(createAnalyzer());
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            return new IndexWriter(createDirectory(directory), config);
        } catch (IOException e) {
            log.error("Can't create index writer", e);
            throw new RuntimeException(e);
        }
    }

    public static IndexSearcher createSearcher(IndexReader reader) {
        return new IndexSearcher(reader);
    }

    public static DirectoryReader createReader(Directory index) throws IOException {
        return DirectoryReader.open(index);
    }
}
