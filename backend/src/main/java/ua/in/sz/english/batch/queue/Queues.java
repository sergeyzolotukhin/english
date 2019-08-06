package ua.in.sz.english.batch.queue;

import java.util.concurrent.ArrayBlockingQueue;

public final class Queues {
    private Queues() {
        // no instance
    }

    public static Queue<Event> createQueue(int capacity) {
        return new QueueImpl(capacity);
    }

    private static class QueueImpl extends ArrayBlockingQueue<Event>
            implements Queue<Event> {

        QueueImpl(int capacity) {
            super(capacity);
        }
    }
}
