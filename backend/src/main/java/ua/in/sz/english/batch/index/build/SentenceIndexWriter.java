package ua.in.sz.english.batch.index.build;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Events;
import ua.in.sz.english.batch.queue.Queue;
import ua.in.sz.english.batch.index.IndexConstant;

import java.io.IOException;

@Slf4j
@Getter
@RequiredArgsConstructor
public class SentenceIndexWriter implements Runnable {
    private final Queue<Event> queue;
    private final IndexWriter indexWriter;
    private final String indexPath;

    @Override
    public void run() {
        try {
            log.debug("Start write index: {}", indexPath);

            doConsume();

            log.debug("End write index: {}", indexPath);
        } catch (InterruptedException | IOException e) {
            log.error("Can't write index", e);
        }
    }

    private void doConsume() throws InterruptedException, IOException {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            Event event = queue.take();

            if (Events.END_EVENT.equals(event)) {
                handleEnd();
                break;
            }

            if (event instanceof SentenceIndexDto) {
                doHandle((SentenceIndexDto) event);
            }
        }
    }

    private void handleEnd() throws IOException {
        indexWriter.commit();
    }

    private void doHandle(SentenceIndexDto sentence) throws IOException {
        indexSentence(sentence.getText());

        if (log.isTraceEnabled()) {
            log.trace("Sentence: {}", sentence.getText());
        }
    }

    private void indexSentence(String sentence) throws IOException {
        Document document = new Document();
        document.add(new TextField(IndexConstant.FIELD_SENTENCE, sentence, Field.Store.YES));
        indexWriter.addDocument(document);
    }
}
