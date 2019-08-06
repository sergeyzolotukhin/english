package ua.in.sz.english.batch.parser.book;

import lombok.Data;
import ua.in.sz.english.batch.queue.Event;

@Data
class PageDto implements Event {
    private final String bookTitle;
    private final int pageNo;
    private final String text;
}
