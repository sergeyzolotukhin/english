package ua.in.sz.english;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
class PageDto {
    static final int LAST_PAGE = 1;

    private final String bookTitle;
    private final int pageNo;
    private final String text;
}
