package ua.in.sz.english;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Application {
    private static final String SOURCE_PATH = "e:/_book/_development/_book/domain-driven-design-distilled.pdf";
    private static final String TARGET_PATH = "K:/projects/english/book.log";
    
    public static void main(String[] args) {
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        ExecutorService consumerPool = Executors.newFixedThreadPool(1);

        BlockingQueue<PageDto> queue = new ArrayBlockingQueue<>(100);
        producerPool.submit(new PageProducer(SOURCE_PATH, queue));
        consumerPool.submit(new PageConsumer(TARGET_PATH, queue));

        producerPool.shutdown();
        consumerPool.shutdown();
    }

}
