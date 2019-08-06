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

@Slf4j
@Getter
@RequiredArgsConstructor
public class SentenceIndexWriter implements Runnable {
    private final BlockingQueue<SentenceIndexDto> queue;
    private final String indexPath;

    @Override
    public void run() {
        try (Analyzer analyzer = IndexFactory.createAnalyzer();
             Directory directory = IndexFactory.createDirectory(indexPath);
             IndexWriter indexWriter = IndexFactory.createIndexWriter(analyzer, directory)
        ) {
            log.info("Start write index: {}", indexPath);

            doConsume(indexWriter);

            log.info("End write index: {}", indexPath);
        } catch (InterruptedException | IOException e) {
            log.error("Can't write index", e);
        }
    }

    private void doConsume(IndexWriter indexWriter) throws InterruptedException, IOException {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            SentenceIndexDto sentence = queue.take();

            if (SentenceIndexDto.LAST.equals(sentence.getText())) {
                break;
            }

            indexSentence(indexWriter, sentence.getText());

            if (log.isTraceEnabled()) {
                log.trace("Sentence: {}", sentence.getText());
            }
        }
    }

    private static void indexSentence(IndexWriter indexWriter, String sentence) throws IOException {
        Document document = new Document();
        document.add(new TextField(IndexConstant.FIELD_SENTENCE, sentence, Field.Store.YES));
        indexWriter.addDocument(document);
    }
}
