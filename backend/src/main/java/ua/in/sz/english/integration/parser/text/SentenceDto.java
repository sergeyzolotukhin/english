package ua.in.sz.english.integration.parser.text;

import lombok.Data;

@Data
public class SentenceDto {
    public static final String LAST = "last sentence";

    private final String bookTitle;
    private final String text;
}
