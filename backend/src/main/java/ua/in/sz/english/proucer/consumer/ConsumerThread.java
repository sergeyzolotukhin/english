package ua.in.sz.english.proucer.consumer;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.Application;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class ConsumerThread extends Thread {
    private final BlockingQueue<Message> queue;

    ConsumerThread(ThreadGroup group, String name, BlockingQueue<Message> queue) {
        super(group, name);
        this.queue = queue;
    }

    @Override
    public void run() {
        log.info("Start");

        try {
            while (true) {
                if (isInterrupted()) {
                    log.info("Canceled");
                    break;
                }

                Message m = queue.take();
                if (m instanceof EndMessage) {
                    log.info("last message");
                    break;
                }

                int no = m.getNo();
                log.info("Receive {}", no);

/*                if (no == 5) {
                    throw new RuntimeException("Exception " + no);
                }*/
            }
        } catch (InterruptedException e) {
            log.info("Interrupted");

            getThreadGroup().interrupt();
        }

        log.info("End");
    }
}
