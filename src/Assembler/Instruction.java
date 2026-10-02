package Assembler;

import com.sun.jdi.event.StepEvent;

import java.util.List;
import java.util.Map;

public class Instruction {

    static final private List<String> R_Type = List.of("add", "nand");
    static final private List<String> I_Type = List.of("lw", "sw","beq");
    static final private List<String> J_Type = List.of("jalr");
    static final private List<String> O_Type = List.of("halt", "noop");
    static final private List<String> Special = List.of( ".fill");
    static private Map<String, String> Opcode = Map.of("add", "000", "nand", "001", "lw", "010", "sw", "011", "beq", "100", "jalr", "101", "halt", "110", "noop", "111");

    public static boolean isInstruction(String instruction) {
        return R_Type.contains(instruction) || I_Type.contains(instruction) || J_Type.contains(instruction) || O_Type.contains(instruction) || Special.contains(instruction);
    }

    public static String opcode(String instruction) {
        return Opcode.get(instruction);
    }
    public static String type(String instruction){
        if(R_Type.contains(instruction))return "R";
        if (I_Type.contains(instruction))return "I";
        if(J_Type.contains(instruction))return "J";
        if(O_Type.contains(instruction))return "O";
        return null;
    }

}
