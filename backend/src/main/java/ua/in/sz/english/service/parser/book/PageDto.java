package ua.in.sz.english.service.parser.book;

import lombok.Data;

@Data
public class PageDto {
    public static final int LAST = -1;

    private final String bookTitle;
    private final int pageNo;
    private final String text;
}
