package ua.in.sz.english.service.search.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class SentenceIndexWriter implements Runnable {
    private final BlockingQueue<IndexDto> queue;
    private final String path;

    @Override
    public void run() {
        try (Analyzer analyzer = IndexUtil.createAnalyzer();
             Directory directory = IndexUtil.createDirectory(path);
             IndexWriter indexWriter = createIndexWriter(analyzer, directory)
        ) {
            doConsume(indexWriter);
        } catch (InterruptedException | IOException e) {
            log.error("Interrupted", e);
        }
    }

    private void doConsume(IndexWriter indexWriter) throws InterruptedException, IOException {
        while (true) {
            IndexDto sentence = queue.take();

            if (IndexDto.LAST.equals(sentence.getText())) {
                log.info("End write index: {}", path);
                break;
            }

            indexSentence(indexWriter, sentence.getText());

            if (log.isTraceEnabled()) {
                log.trace("Sentence: {}", sentence.getText());
            }
        }
    }

    private static void indexSentence(IndexWriter index, String sentence) throws IOException {
        Document doc = new Document();
        doc.add(new TextField(IndexUtil.FIELD_SENTENCE, sentence, Field.Store.YES));
        index.addDocument(doc);
    }

    private IndexWriter createIndexWriter(Analyzer analyzer, Directory directory) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(directory, config);
    }
}
