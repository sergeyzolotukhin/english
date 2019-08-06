package ua.in.sz.english.batch.index.build;

import lombok.Data;
import ua.in.sz.english.batch.queue.Event;

@Data
class SentenceIndexDto implements Event {
    private final String bookTitle;
    private final String text;
}
