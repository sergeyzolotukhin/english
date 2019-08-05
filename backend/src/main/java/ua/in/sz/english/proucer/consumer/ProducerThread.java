package ua.in.sz.english.proucer.consumer;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.Application;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class ProducerThread extends Thread {
    private final BlockingQueue<Message> queue;

    ProducerThread(ThreadGroup group, String name, BlockingQueue<Message> queue) {
        super(group, name);
        this.queue = queue;
    }

    @Override
    public void run() {
        log.info("Start");

        try {
            for (int no = 0; no < 10; no++) {
                if (isInterrupted()) {
                    log.info("Canceled");
                    return;
                }

                sleep(100);

                log.info("send {}", no);
                queue.put(new Message(no));

//                if (no == 5) {
//                    throw new RuntimeException("Exception " + no);
//                }
            }

            queue.put(new EndMessage(-1));
        } catch (InterruptedException e) {
            log.info("Interrupted");

            getThreadGroup().interrupt();
        }

        log.info("End");
    }
}
