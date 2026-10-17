package Simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Simulator {

    public static int convertNum(int num) {
        if ((num & (1 << 15)) != 0) {
            num -= (1 << 16);
        }
        return num;
    }

    public static void printState(StateType statePtr) {
        int i;
        System.out.printf("\n@@@\nstate:\n");
        System.out.printf("\tpc %d\n", statePtr.pc);
        System.out.printf("\tmemory:\n");
        for (i = 0; i < statePtr.numMemory; i++) {
            System.out.printf("\t\tmem[ %d ] %d\n", i, statePtr.mem[i]);
        }
        System.out.printf("\tregisters:\n");
        for (i = 0; i < StateType.NUMREGS; i++) {
            System.out.printf("\t\treg[ %d ] %d\n", i, statePtr.reg[i]);
        }
        System.out.printf("end state\n");
    }

    public static void loadMachineCode(StateType state, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.printf("error: can't open file %s\n", filePath);
            System.exit(1);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            state.numMemory = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    int val = Integer.parseInt(line);
                    state.mem[state.numMemory] = val;
                    System.out.printf("memory[%d]=%d\n", state.numMemory, state.mem[state.numMemory]);
                    state.numMemory++;
                } catch (NumberFormatException e) {
                    System.err.printf("error in reading address %d\n", state.numMemory);
                    System.exit(1);
                }
            }
            System.out.printf("\n");
        } catch (IOException e) {
            System.err.printf("error in reading file %s: %s\n", filePath, e.getMessage());
            System.exit(1);
        }
    }

    public static void loadMachineCode(StateType state, List<String> machineCodes) {
        state.numMemory = 0;
        for (String line : machineCodes) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                int val = Integer.parseInt(line);
                state.mem[state.numMemory] = val;
                System.out.printf("memory[%d]=%d\n", state.numMemory, state.mem[state.numMemory]);
                state.numMemory++;
            } catch (NumberFormatException e) {
                System.err.printf("error in reading address %d\n", state.numMemory);
                System.exit(1);
            }
        }
        System.out.printf("\n");
    }

    public static StateType simulate(String filePath) {
        StateType state = new StateType();
        loadMachineCode(state, filePath);
        return runSimulation(state);
    }

    public static StateType simulate(List<String> machineCodes) {
        StateType state = new StateType();
        loadMachineCode(state, machineCodes);
        return runSimulation(state);
    }

    public static StateType runSimulation(StateType state) {
        int instructionsExecuted = 0;
        boolean halted = false;

        while (!halted) {
            printState(state);

            if (state.pc < 0 || state.pc >= StateType.NUMMEMORY) {
                System.err.printf("error: pc out of memory bounds: %d\n", state.pc);
                System.exit(1);
            }

            int inst = state.mem[state.pc];
            int opcode = (inst >> 22) & 0x7;
            int regA = (inst >> 19) & 0x7;
            int regB = (inst >> 16) & 0x7;

            instructionsExecuted++;

            switch (opcode) {
                case 0: { // add (R-type)
                    int destReg = inst & 0x7;
                    state.pc++;
                    state.reg[destReg] = state.reg[regA] + state.reg[regB];
                    break;
                }
                case 1: { // nand (R-type)
                    int destReg = inst & 0x7;
                    state.pc++;
                    state.reg[destReg] = ~(state.reg[regA] & state.reg[regB]);
                    break;
                }
                case 2: { // lw (I-type)
                    int offset = convertNum(inst & 0xFFFF);
                    int address = state.reg[regA] + offset;
                    if (address < 0 || address >= StateType.NUMMEMORY) {
                        System.err.printf("error: memory address out of bounds: %d\n", address);
                        System.exit(1);
                    }
                    state.pc++;
                    state.reg[regB] = state.mem[address];
                    break;
                }
                case 3: { // sw (I-type)
                    int offset = convertNum(inst & 0xFFFF);
                    int address = state.reg[regA] + offset;
                    if (address < 0 || address >= StateType.NUMMEMORY) {
                        System.err.printf("error: memory address out of bounds: %d\n", address);
                        System.exit(1);
                    }
                    state.pc++;
                    state.mem[address] = state.reg[regB];
                    break;
                }
                case 4: { // beq (I-type)
                    int offset = convertNum(inst & 0xFFFF);
                    state.pc++;
                    if (state.reg[regA] == state.reg[regB]) {
                        state.pc += offset;
                    }
                    break;
                }
                case 5: { // jalr (J-type)
                    int nextPC = (regA == regB) ? (state.pc + 1) : state.reg[regA];
                    state.reg[regB] = state.pc + 1;
                    state.pc = nextPC;
                    break;
                }
                case 6: { // halt (O-type)
                    state.pc++;
                    halted = true;
                    break;
                }
                case 7: { // noop (O-type)
                    state.pc++;
                    break;
                }
                default: {
                    System.err.printf("error: illegal opcode %d\n", opcode);
                    System.exit(1);
                }
            }

            state.reg[0] = 0;
        }

        System.out.printf("machine halted\n");
        System.out.printf("total of %d instructions executed\n", instructionsExecuted);
        System.out.printf("final state of machine:\n");

        printState(state);

        return state;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            simulate(args[0]);
        } else {
            simulate("src/Input/Simulator_input.txt");
        }
    }
}
