package ua.in.sz.english;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.parser.pdf.PdfPageConsumer;
import ua.in.sz.english.parser.pdf.PdfPageDto;
import ua.in.sz.english.parser.pdf.PdfPageProducer;
import ua.in.sz.english.tokenizer.sentence.SentenceConsumer;
import ua.in.sz.english.tokenizer.sentence.SentenceDto;
import ua.in.sz.english.tokenizer.sentence.SentenceProducer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Application {
    private static final String PDF_BOOK_PATH = "e:/_book/_development/_book/domain-driven-design-distilled.pdf";
    private static final String TEXT_BOOK_PATH = "K:/projects/english/book.log";
    
    public static void main(String[] args) {
//        pdfToText();

        textToSentence();
    }

    private static void textToSentence() {
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        ExecutorService consumerPool = Executors.newFixedThreadPool(1);

        BlockingQueue<SentenceDto> queue = new ArrayBlockingQueue<>(100);
        producerPool.submit(new SentenceProducer(TEXT_BOOK_PATH, queue));
        consumerPool.submit(new SentenceConsumer("", queue));

        producerPool.shutdown();
        consumerPool.shutdown();
    }

    private static void pdfToText() {
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        ExecutorService consumerPool = Executors.newFixedThreadPool(1);

        BlockingQueue<PdfPageDto> queue = new ArrayBlockingQueue<>(100);
        producerPool.submit(new PdfPageProducer(PDF_BOOK_PATH, queue));
        consumerPool.submit(new PdfPageConsumer(TEXT_BOOK_PATH, queue));

        producerPool.shutdown();
        consumerPool.shutdown();
    }

}
