package ua.in.sz.english.batch.queue;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public final class Events {
    private Events() {
        // no instance
    }

    public static final SimpleEvent START_EVENT = new SimpleEvent(0);
    public static final SimpleEvent END_EVENT = new SimpleEvent(1);

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @SuppressWarnings("WeakerAccess")
    public static final class SimpleEvent implements Event {
        private final int code;
    }
}
