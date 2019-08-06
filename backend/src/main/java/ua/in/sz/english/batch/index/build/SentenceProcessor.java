package ua.in.sz.english.batch.index.build;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.task.TaskExecutor;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Queue;
import ua.in.sz.english.batch.queue.Queues;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SentenceProcessor implements ItemWriter<File> {
    private final AppProps props;
    private final TaskExecutor executor;
    private final IndexWriter indexWriter;

    public SentenceProcessor(TaskExecutor executor, IndexWriter indexWriter, AppProps props) {
        this.executor = executor;
        this.indexWriter = indexWriter;
        this.props = props;
    }

    @Override
    public void write(List<? extends File> files) throws Exception {
        for (File file : files) {
            parse(file);
        }
    }

    private void parse(File sentence) throws ExecutionException, InterruptedException {
        log.debug("Parse sentence: {}", sentence);

        Queue<Event> queue = Queues.createQueue(props.getSentenceParseQueueCapacity());
        SentenceReader parser = new SentenceReader(queue, sentence.getPath());
        SentenceIndexWriter writer = new SentenceIndexWriter(queue, indexWriter, props.getIndexDirPath());

        CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, executor),
                CompletableFuture.runAsync(writer, executor)).get();

        log.debug("End parse sentence: {}", sentence);
    }
}
