package ua.in.sz.english.service.parser.pdf;

import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

class PdfPageReader extends PdfReader implements AutoCloseable {
    PdfPageReader(String filename) throws IOException {
        super(filename);
    }
}