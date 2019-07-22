package ua.in.sz.english.service.search.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public final class IndexUtil {
    public static final String FIELD_SENTENCE = "path";

    private IndexUtil() {
        // no instance
    }

    public static FSDirectory createDirectory(String path) throws IOException {
        return FSDirectory.open(Paths.get(path));
    }

    public static StandardAnalyzer createAnalyzer() {
        return new StandardAnalyzer();
    }
}
