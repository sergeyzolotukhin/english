package ua.in.sz.english;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "english")
public class AppProps {
    /**
     * Location of source book
     */
    @NotEmpty
    private String bookDirPath;
    @NotEmpty
    private String textDirPath;
    @NotEmpty
    private String sentenceDirPath;
    @NotEmpty
    private String indexDirPath;

    private int bookParseQueueCapacity = 20;
    private int textParseQueueCapacity = 1000;

    public String getBookDirPath() {
        return bookDirPath;
    }

    public void setBookDirPath(String bookDirPath) {
        this.bookDirPath = bookDirPath;
    }

    public String getTextDirPath() {
        return textDirPath;
    }

    public void setTextDirPath(String textDirPath) {
        this.textDirPath = textDirPath;
    }

    public String getSentenceDirPath() {
        return sentenceDirPath;
    }

    public void setSentenceDirPath(String sentenceDirPath) {
        this.sentenceDirPath = sentenceDirPath;
    }

    public String getIndexDirPath() {
        return indexDirPath;
    }

    public void setIndexDirPath(String indexDirPath) {
        this.indexDirPath = indexDirPath;
    }

    public int getBookParseQueueCapacity() {
        return bookParseQueueCapacity;
    }

    public void setBookParseQueueCapacity(int bookParseQueueCapacity) {
        this.bookParseQueueCapacity = bookParseQueueCapacity;
    }

    public int getTextParseQueueCapacity() {
        return textParseQueueCapacity;
    }

    public void setTextParseQueueCapacity(int textParseQueueCapacity) {
        this.textParseQueueCapacity = textParseQueueCapacity;
    }
}
