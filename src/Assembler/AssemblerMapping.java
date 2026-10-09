package Assembler;

import Exceptions.SyntaxError;

import java.util.Objects;

public class AssemblerMapping {
    public static Assembly mapAssembly(String assembly) throws SyntaxError {
        Tokenizer rawAssembly = new AssemblyTokenizer(assembly);
        Assembly mapped_Assembly = new Assembly();
        String first = rawAssembly.consume();
        if (first.equals("noop") || first.equals("halt")) {
            mapped_Assembly.instruction = first;
            return mapped_Assembly;
        }

        String second = rawAssembly.peek();
        if (Instruction.isInstruction(first) && !Instruction.isInstruction(second)) {
            mapped_Assembly.instruction = first;
            second = first;
        } else if (!Instruction.isInstruction(first) && Instruction.isInstruction(second)) {

            mapped_Assembly.label = first;
            mapped_Assembly.instruction = second;
            rawAssembly.consume();
        } else throw new SyntaxError("instruction not found");
        if (second.equals("halt")) return mapped_Assembly;
        String third = rawAssembly.consume();
        if (second.equals(".fill")) {
            mapped_Assembly.field0 = third;
            return mapped_Assembly;
        }
        mapped_Assembly.field0 = third;
        mapped_Assembly.field1 = rawAssembly.consume();

        if (Objects.equals(Instruction.type(second), "J")) {
            return mapped_Assembly;
        }


        mapped_Assembly.field2 = rawAssembly.consume();
        return mapped_Assembly;
    }


}
