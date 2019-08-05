package ua.in.sz.english.proucer.consumer;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class ConsumerProducerApplication {
    public static void main(String[] args) throws InterruptedException {
        log.info("Start");

        BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
        ThreadGroup group = new ConsumerProducerThreadGroup("group-1");

        ProducerThread producer = new ProducerThread(group, "producer-1", queue);
        ConsumerThread consumer = new ConsumerThread(group, "consumer-1", queue);

        consumer.start();
        producer.start();

        consumer.join();
        producer.join();

        log.info("End");
    }
}
