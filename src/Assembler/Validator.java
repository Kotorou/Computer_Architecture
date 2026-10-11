package Assembler;

import Exceptions.SyntaxError;

public class Validator {
    static public void registerValidate(String register) throws SyntaxError {
        if (!register.matches("[0-7]")) throw new SyntaxError("invalid register");
    }

    static public void numberValidate(String number) throws SyntaxError {
        if (!number.matches("-?(0|[1-9]\\d*)")) throw new SyntaxError("NAN");
        try {
            int val = Integer.parseInt(number);
            if (val < -32768 || val > 32767) throw new SyntaxError("number out of range");
        } catch (NumberFormatException e) {
            throw new SyntaxError("number out of range");
        }
    }
    static public void fillValidate(String number) throws SyntaxError {
        if (!number.matches("-?(0|[1-9]\\d*)")) throw new SyntaxError("NAN");
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new SyntaxError("number out of range");
        }
    }
}
