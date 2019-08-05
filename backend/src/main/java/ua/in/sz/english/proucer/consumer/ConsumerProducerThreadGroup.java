package ua.in.sz.english.proucer.consumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerProducerThreadGroup extends ThreadGroup {
    ConsumerProducerThreadGroup(String name) {
        super(name);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Process error:", e);

        interrupt();
    }
}
