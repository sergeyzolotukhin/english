package ua.in.sz.english;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Application {
    private static final String PATH =
//            "e:/share/_spark/getting_started_with_apache_spark.pdf"
            "e:/_book/_development/_book/domain-driven-design-distilled.pdf";

    public static void main(String[] args) {
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        ExecutorService consumerPool = Executors.newFixedThreadPool(1);

        BlockingQueue<PageDto> queue = new ArrayBlockingQueue<>(100);
        producerPool.submit(new PageProducer(PATH, queue));
        consumerPool.submit(new PageConsumer(queue));

        producerPool.shutdown();
        consumerPool.shutdown();
    }

}
