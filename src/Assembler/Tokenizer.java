package Assembler;
import Exceptions.SyntaxError;
public interface Tokenizer {

    /** Returns true if there is
     *  more token */
    boolean hasNextToken();

    /** Returns the next token
     *  in the input stream. */
    String peek();
    boolean peek(String s);
    /** Consumes the next token
     *  from the input stream
     *  and returns it.
     *  effects: removes the next token
     *           from the input stream */
    String consume();
    void consume(String s) throws SyntaxError;
}
