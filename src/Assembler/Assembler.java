package Assembler;

import Exceptions.SyntaxError;
import Tools.BinaryTool;

import java.util.*;


public class Assembler {


    private final List<Assembly> assembles;
    private final Map<String, String> assignment = new HashMap<>();
    private final Map<String, String> addresses = new HashMap<>();


    public Assembler(List<Assembly> assembles) {
        this.assembles = assembles;

    }

    public List<String> toMachineCode() throws SyntaxError {
        List<String> machineCode = new ArrayList<>();
        assign();
        String binaryMC = "";
        for (Assembly assembly : assembles) {

            if (assembly.instruction.equals(".fill")) {
                if (addresses.containsKey(assembly.field0)) {
                    machineCode.add(assignment.get(assembly.label));
                } else machineCode.add(assembly.field0);
                continue;
            }

            String opcode = Instruction.opcode(assembly.instruction);
            String regA = "";
            String regB = "";
            String destReg = "";
            String empty = "";
            if (Objects.equals(Instruction.type(assembly.instruction), "O")) {
                empty = BinaryTool.signExtension("0", 22);
                binaryMC = opcode + empty;
                machineCode.add(String.valueOf(Integer.parseInt(binaryMC, 2)));
                continue;
            }
            Validator.registerValidate(assembly.field0);
            Validator.registerValidate(assembly.field1);
            regA = BinaryTool.unSignExtension(BinaryTool.twoCompliment(assembly.field0), 3);
            regB = BinaryTool.unSignExtension(BinaryTool.twoCompliment(assembly.field1), 3);
            if (Objects.equals(Instruction.type(assembly.instruction), "R")) {
                destReg = BinaryTool.unSignExtension(BinaryTool.twoCompliment(assembly.field2), 3);
                empty = BinaryTool.signExtension("0", 13);
                binaryMC = opcode + regA + regB + empty + destReg;

            } else if (Objects.equals(Instruction.type(assembly.instruction), "J")) {
                empty = BinaryTool.signExtension("0", 16);
                binaryMC = opcode + regA + regB + empty;

            } else if (Objects.equals(Instruction.type(assembly.instruction), "I")) {
                String field2 = "";
                if (!assembly.field2.matches("-?(0|[1-9]\\d*)")) {
                    if (addresses.containsKey(assembly.field2)) {
                        if (assembly.instruction.equals("beq")) {
                            field2 = String.valueOf(Integer.parseInt(addresses.get(assembly.field2)) - assembly.address - 1);

                        } else {
                            field2 = addresses.get(assembly.field2);
                        }
                    } else
                        throw new SyntaxError("label unidentified");
                } else {
                    Validator.numberValidate(assembly.field2);
                    field2 = assembly.field2;
                }

                destReg = BinaryTool.signExtension(BinaryTool.twoCompliment(field2), 16);
                binaryMC = opcode + regA + regB + destReg;

            }
            machineCode.add(String.valueOf(Integer.parseInt(binaryMC, 2)));

        }

        return machineCode;
    }

    private void assign() throws SyntaxError {
        int address = 0;
        //assign address
        for (Assembly assembly : assembles) {
            assembly.address = address;
            address++;

            if (!assembly.label.isEmpty()) {
                if (!addresses.containsKey(assembly.label)) {
                    addresses.put(assembly.label, String.valueOf(assembly.address));
                } else throw new SyntaxError("duplicate label");
            }
        }
        //assign parameter
        for (Assembly assembly : assembles) {
            if (!assembly.label.isEmpty()) {
                if (!assignment.containsKey(assembly.label)) {
                    if (assembly.instruction.equals(".fill")) {

                        if (assembly.field0.matches("-?(0|[1-9]\\d*)")) {
                            Validator.numberValidate(assembly.field0);
                            assignment.put(assembly.label, assembly.field0);
                        } else if (addresses.containsKey(assembly.field0)) {
                            assignment.put(assembly.label, addresses.get(assembly.field0));
                        } else throw new SyntaxError("address unidentified");
                    }

                } else throw new SyntaxError("duplicate label");
            }
        }




        //System.out.println("Var: " + assignment.toString());
        //System.out.println("Address: " + addresses.toString());
    }

    public static void AssemblyToMachineCode(String inputPath, String OutputPath) {
        ReaderAndWrite r = new ReaderAndWrite();
        try {
            List<String> code = r.read(inputPath);
            List<Assembly> assemblyCode = new ArrayList<>();
            for (String c : code) {
                Assembly map = AssemblerMapping.mapAssembly(c);
                //  map.printInstruction();
                assemblyCode.add(map);
            }
            List<String> decimalAssembly = new Assembler(assemblyCode).toMachineCode();
            r.write(OutputPath, decimalAssembly);
            System.out.println("exit(0)");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}
