package ua.in.sz.english.batch.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BookSource implements ItemReader<File> {
    private static final String FILE_PATTERN = "*.pdf";

    private final String path;
    private final Iterator<File> iterator;

    public BookSource(String path) {
        this.path = path;
        this.iterator = getFiles().iterator();
    }

    @Override
    public File read() {
        if (iterator.hasNext()) {
            return iterator.next();
        }

        return null;
    }

    private List<File> getFiles() {
        try {
            return Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Can't read files", e);

            return Collections.emptyList();
        }
    }
}
