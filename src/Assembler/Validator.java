package Assembler;

import Exceptions.SyntaxError;

public class Validator {
    static public void registerValidate(String register) throws SyntaxError {
        if (!register.matches("[0-7]")) throw new SyntaxError("invalid register");
    }

    static public void numberValidate(String number) throws SyntaxError {
        if (!number.matches("(-*[1-9]+\\d*)|(0)")) throw new SyntaxError("NAN");
        int val = Integer.parseInt(number);
        if (val < -32768 || val > 32767) throw new SyntaxError("number out of range");
    }
}
