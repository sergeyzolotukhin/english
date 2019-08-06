package ua.in.sz.english.integration.index.build;

import lombok.Data;

@Data
public class SentenceIndexDto {
    public static final String LAST = "last sentence";

    private final String bookTitle;
    private final String text;
}
