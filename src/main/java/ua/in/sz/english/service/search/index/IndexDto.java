package ua.in.sz.english.service.search.index;

import lombok.Data;

@Data
public class IndexDto {
    public static final String LAST = "last sentence";

    private final String bookTitle;
    private final String text;
}
