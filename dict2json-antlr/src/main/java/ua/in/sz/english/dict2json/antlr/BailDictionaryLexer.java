package ua.in.sz.english.dict2json.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;

public class BailDictionaryLexer extends DictionaryLexer {
    public BailDictionaryLexer(CharStream input) {
        super(input);
    }

    @Override
    public void recover(LexerNoViableAltException e) {
        throw new RuntimeException(e);
    }
}
