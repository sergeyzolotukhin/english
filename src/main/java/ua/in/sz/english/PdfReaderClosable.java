package ua.in.sz.english;

import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

public class PdfReaderClosable extends PdfReader implements AutoCloseable {
    PdfReaderClosable(String filename) throws IOException {
        super(filename);
    }
}