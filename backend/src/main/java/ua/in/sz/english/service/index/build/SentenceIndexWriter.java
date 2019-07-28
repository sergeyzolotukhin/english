package ua.in.sz.english.service.index.build;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import ua.in.sz.english.service.index.IndexConstant;
import ua.in.sz.english.service.index.IndexFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Getter
@RequiredArgsConstructor
public class SentenceIndexWriter implements Runnable {
    private final BlockingQueue<SentenceIndexDto> queue;
    private final String path;

    private AtomicInteger expected = new AtomicInteger(Integer.MAX_VALUE);

    @Override
    public void run() {
        try (Analyzer analyzer = IndexFactory.createAnalyzer();
             Directory directory = IndexFactory.createDirectory(path);
             IndexWriter indexWriter = IndexFactory.createIndexWriter(analyzer, directory)
        ) {
            doConsume(indexWriter);
        } catch (InterruptedException | IOException e) {
            log.error("Interrupted", e);
        }
    }

    private void doConsume(IndexWriter indexWriter) throws InterruptedException, IOException {
        log.info("Start write index");

        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            SentenceIndexDto sentence = queue.take();

            if (SentenceIndexDto.LAST.equals(sentence.getText())) {
                if (expected.decrementAndGet() <= 0) {
                    log.info("End write build: {}", path);
                    break;
                }
            }

            indexSentence(indexWriter, sentence.getText());

            if (log.isTraceEnabled()) {
                log.trace("Sentence: {}", sentence.getText());
            }
        }
    }

    public void expectedCount(int count) {
        if (expected.addAndGet(count - Integer.MAX_VALUE) <= 0) {
            Thread.currentThread().interrupt();
        }
    }

    private static void indexSentence(IndexWriter indexWriter, String sentence) throws IOException {
        Document document = new Document();
        document.add(new TextField(IndexConstant.FIELD_SENTENCE, sentence, Field.Store.YES));
        indexWriter.addDocument(document);
    }
}
