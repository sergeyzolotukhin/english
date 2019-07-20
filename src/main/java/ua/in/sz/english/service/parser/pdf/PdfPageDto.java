package ua.in.sz.english.service.parser.pdf;

import lombok.Data;

@Data
public class PdfPageDto {
    static final int LAST = 1;

    private final String bookTitle;
    private final int pageNo;
    private final String text;
}
