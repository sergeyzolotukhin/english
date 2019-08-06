package ua.in.sz.english.integration.processor;

import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import ua.in.sz.english.AppProps;

import java.io.File;
import java.util.Arrays;

public class BookFileSource extends FileReadingMessageSource {
    private static final String FILE_PATTERN = "*.pdf";

    public BookFileSource(AppProps appProps) {
        setDirectory(new File(appProps.getBookDirPath()));
        setFilter(
                new CompositeFileListFilter<>(Arrays.asList(
                        new AcceptOnceFileListFilter<>(),
                        new SimplePatternFileListFilter(FILE_PATTERN))));
    }
}
