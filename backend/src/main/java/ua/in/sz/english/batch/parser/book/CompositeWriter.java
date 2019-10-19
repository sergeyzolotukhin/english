package ua.in.sz.english.batch.parser.book;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Events;
import ua.in.sz.english.batch.queue.Queue;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CompositeWriter<T extends Event> implements Runnable {
	private final Queue<T> queue;
	private final List<ConsumerWriter<T>> consumers = new ArrayList<>();

	public CompositeWriter(Queue<T> queue) {
		this.queue = queue;
	}

	public void addConsumer(ConsumerWriter<T> consumer) {
		consumers.add(consumer);
	}

	@Override
	public void run() {
		try {
			consumers.forEach(ConsumerWriter::start);

			doConsume();

		} catch (InterruptedException e) {
			log.info("Consumer interrupted");
		} finally {
			consumers.forEach(ConsumerWriter::stop);
		}
	}

	private void doConsume() throws InterruptedException {
		while (true) {
			T event = queue.take();

			if (Events.END_EVENT.equals(event)) {
				break;
			}

			consumers.forEach(consumer -> consumer.accept(event));
		}
	}
}
