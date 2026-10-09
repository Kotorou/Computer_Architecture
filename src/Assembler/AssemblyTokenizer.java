package Assembler;


import Exceptions.SyntaxError;

import java.util.NoSuchElementException;
import java.util.Set;

import static java.lang.Character.isDigit;

public class AssemblyTokenizer implements Tokenizer{


    private String src, next;
    private int pos;

    public AssemblyTokenizer (String src) {
        this.src = src;
        pos = 0;
        computeNext();
    }

    public boolean hasNextToken() {
        return next != null;
    }

    public void checkNextToken() {
        if (!hasNextToken()) throw new
                NoSuchElementException("no more tokens");
    }

    public String peek() {
        checkNextToken();
        return next;
    }

    public String consume() {
        checkNextToken();
        String result = next;
        computeNext();
        return result;
    }

    private void computeNext()   {
        StringBuilder s = new StringBuilder();
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos)))
            pos++;  // ignore whitespace
        if (pos == src.length()) {
            next = null;
            return;
        }  // no more tokens
        char c = src.charAt(pos);

            s.append(c);
            for (pos++; pos < src.length() &&
                    !String.valueOf(src.charAt(pos)).matches("\\s"); pos++)
                s.append(src.charAt(pos));

        next = s.toString();
    }

    public boolean peek(String s) {
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }


    public void consume(String s)


            throws SyntaxError {
        if (peek(s))
            consume();
        else
            throw new SyntaxError(s + " expected");
    }

}
