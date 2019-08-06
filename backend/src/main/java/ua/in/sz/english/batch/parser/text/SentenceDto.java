package ua.in.sz.english.batch.parser.text;

import lombok.Data;
import ua.in.sz.english.batch.queue.Event;

@Data
class SentenceDto implements Event {
    private final String bookTitle;
    private final String text;
}
