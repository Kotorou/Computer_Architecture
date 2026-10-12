package Simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Simulator {

    public static int convertNum(int num) { //แปลงเลข 16-bit 2's complement เป็น 32-bit integer
        if ((num & (1 << 15)) != 0) { //ตรวจสอบว่าบิตที่ 15 เป็น 1 หรือไม่ (ติดลบ)
            num -= (1 << 16); //ถ้าเป็นลบ ให้ลบด้วย 65536
        }
        return num; //คืนค่าตัวเลข 32-bit
    }

    public static void printState(StateType statePtr) { //แสดงสถานะ
        int i;
        System.out.printf("\n@@@\nstate:\n");
        System.out.printf("\tpc %d\n", statePtr.pc); //Program Counter ปัจจุบัน
        System.out.printf("\tmemory:\n");   
        for (i = 0; i < statePtr.numMemory; i++) { //วนลูปตามจำนวนบรรทัดของ Memory
            System.out.printf("\t\tmem[ %d ] %d\n", i, statePtr.mem[i]); //พิมพ์ข้อมูลใน Memory แต่ละตำแหน่ง
        }
        System.out.printf("\tregisters:\n");
        for (i = 0; i < StateType.NUMREGS; i++) { //reg[0]-reg[7]
            System.out.printf("\t\treg[ %d ] %d\n", i, statePtr.reg[i]);
        }
        System.out.printf("end state\n");
    }

    public static void loadMachineCode(StateType state, String filePath) { //อ่าน Machine Code จากไฟล์ลง Memory
        File file = new File(filePath);
        if (!file.exists()) { //ตรวจสอบว่าไฟล์มีอยู่จริงหรือไม่
            System.err.printf("error: can't open file %s\n", filePath); //ไม่พบ
            System.exit(1);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { //อ่านไฟล์
            String line; //เก็บข้อความแต่ละบรรทัด
            state.numMemory = 0; //ค่าเริ่มต้นของจำนวนบรรทัด Memory
            while ((line = reader.readLine()) != null) { //อ่านไฟล์ทีละบรรทัดจนกว่าจะหมด
                line = line.trim(); //ตัดช่องว่างส่วนเกินที่หัวและท้ายข้อความออก
                if (line.isEmpty()) { //ตรวจสอบถ้าเป็นบรรทัดว่าง
                    continue;
                }
                try { //ดักจับข้อผิดพลาดการแปลงตัวเลข
                    int val = Integer.parseInt(line); //แปลงข้อความในบรรทัดเป็นจำนวนเต็ม 32-bit
                    state.mem[state.numMemory] = val; //เก็บใน Memory
                    System.out.printf("memory[%d]=%d\n", state.numMemory, state.mem[state.numMemory]);
                    state.numMemory++;
                } catch (NumberFormatException e) { //ดักจับกรณีข้อความไม่ใช่ตัวเลขที่ถูกต้อง
                    System.err.printf("error in reading address %d\n", state.numMemory);
                    System.exit(1);
                }
            }
            System.out.printf("\n");
        } catch (IOException e) { //I/O
            System.err.printf("error in reading file %s: %s\n", filePath, e.getMessage());
            System.exit(1);
        }
    }

    public static void loadMachineCode(StateType state, List<String> machineCodes) { //โหลด Machine Code จาก List<String>
        state.numMemory = 0;
        for (String line : machineCodes) { //ดึงข้อความคำสั่งแต่ละบรรทัดจาก List
            line = line.trim();
            if (line.isEmpty()) { // ตรวจสอบหากเป็นบรรทัดว่าง
                continue;
            }
            try { //ดักจับข้อผิดพลาดการแปลงตัวเลข
                int val = Integer.parseInt(line); //แปลงข้อความเป็นตัวเลขจำนวนเต็ม 32-bit
                state.mem[state.numMemory] = val;
                System.out.printf("memory[%d]=%d\n", state.numMemory, state.mem[state.numMemory]);
                state.numMemory++;
            } catch (NumberFormatException e) { //ดักจับกรณีข้อความไม่ใช่ตัวเลขที่ถูกต้อง
                System.err.printf("error in reading address %d\n", state.numMemory);
                System.exit(1);
            }
        }
        System.out.printf("\n");
    }

    public static StateType simulate(String filePath) { //จำลองการทำงานจาก filePath
        StateType state = new StateType(); //สร้าง StateType ใหม่
        loadMachineCode(state, filePath); //โหลด Machine Code จากไฟล์ลงสู่ state
        return runSimulation(state); //จำลองและส่งคืนสถานะสุดท้าย
    }

    public static StateType simulate(List<String> machineCodes) { //จำลองการทำงานจาก List ของ Machine Code
        StateType state = new StateType(); //สร้าง StateType ใหม่
        loadMachineCode(state, machineCodes); //โหลด Machine Code จาก List ลงสู่ state
        return runSimulation(state); //จำลองและส่งคืนสถานะสุดท้าย
    }

    public static StateType runSimulation(StateType state) { //sim
        int instructionsExecuted = 0; //จำนวนคำสั่งที่ทำ
        boolean halted = false; //สถานะบอกว่าหยุดทำงานแล้วหรือยัง

        while (!halted) { //วนลูปไปเรื่อยๆ จนกว่า halt จะเป็น true
            printState(state); //printState ก่อนทำ

            if (state.pc < 0 || state.pc >= StateType.NUMMEMORY) { //ตรวจสอบว่า PC อยู่ในขอบเขต 0 ถึง 65535 หรือไม่
                System.err.printf("error: pc out of memory bounds: %d\n", state.pc);
                System.exit(1);
            }

            int inst = state.mem[state.pc]; //ดึงคำสั่ง 32-bit จากตำแหน่งที่ PC ที่ชี้อยู่ใน Memory
            int opcode = (inst >> 22) & 0x7; //เลื่อนบิตไปทางขวา 22 บิต และ AND ด้วย 0x7 เพื่อดึง Opcode (บิต 24-22)
            int regA = (inst >> 19) & 0x7; //เลื่อนบิตไปทางขวา 19 บิต และ AND ด้วย 0x7 เพื่อดึงหมายเลข regA (บิต 21-19)
            int regB = (inst >> 16) & 0x7; //เลื่อนบิตไปทางขวา 16 บิต และ AND ด้วย 0x7 เพื่อดึงหมายเลข regB (บิต 18-16)

            instructionsExecuted++; //เพิ่มจำนวนคำสั่งที่รันไปแล้วขึ้น 1

            switch (opcode) { //ประมวลผลตามค่า Opcode
                case 0: { //คำสั่ง add (R-type, opcode = 000)
                    int destReg = inst & 0x7; //ดึงหมายเลข destReg จากบิต 2-0 (AND ด้วย 0x7)
                    state.pc++; //เพิ่มค่า PC ขึ้น 1 เพื่อชี้คำสั่งถัดไป
                    state.reg[destReg] = state.reg[regA] + state.reg[regB]; //นำค่าใน regA บวกกับ regB แล้วเก็บที่ destReg
                    break;
                }
                case 1: { //คำสั่ง nand (R-type, opcode = 001)
                    int destReg = inst & 0x7; //ดึงหมายเลข destReg จากบิต 2-0 (AND ด้วย 0x7)
                    state.pc++; //เพิ่มค่า PC ขึ้น 1
                    state.reg[destReg] = ~(state.reg[regA] & state.reg[regB]); //นำค่าใน regA AND กับ regB แล้วกลับบิต
                    break;
                }
                case 2: { //คำสั่ง lw (I-type, opcode = 010)
                    int offset = convertNum(inst & 0xFFFF); //ดึง 16 บิตล่าง แล้วทำ Sign Extension
                    int address = state.reg[regA] + offset; //คำนวณ address จาก regA + offset
                    if (address < 0 || address >= StateType.NUMMEMORY) { //ตรวจสอบว่า address อยู่ในขอบเขตของ Memory หรือไม่
                        System.err.printf("error: memory address out of bounds: %d\n", address);
                        System.exit(1);
                    }
                    state.pc++; //เพิ่มค่า PC ขึ้น 1
                    state.reg[regB] = state.mem[address]; //โหลดค่าข้อมูลจาก Memory ตำแหน่ง address มาใส่ใน regB
                    break;
                }
                case 3: { //คำสั่ง sw (I-type, opcode = 011)
                    int offset = convertNum(inst & 0xFFFF); //ดึง 16 บิตล่าง แล้วทำ Sign Extension
                    int address = state.reg[regA] + offset; //คำนวณ address จาก regA + offset
                    if (address < 0 || address >= StateType.NUMMEMORY) { //ตรวจสอบว่า address อยู่ในขอบเขต Memory หรือไม่
                        System.err.printf("error: memory address out of bounds: %d\n", address);
                        System.exit(1);
                    }
                    state.pc++; //เพิ่มค่า PC ขึ้น 1
                    state.mem[address] = state.reg[regB]; //นำค่าจาก regB ไปใส่ใน Memory ตำแหน่ง address
                    break;
                }
                case 4: { //คำสั่ง beq (I-type, opcode = 100)
                    int offset = convertNum(inst & 0xFFFF); //ดึง 16 บิตล่าง แล้วทำ Sign Extension
                    state.pc++; //เลื่อน PC ไปคำสั่งถัดไปก่อน
                    if (state.reg[regA] == state.reg[regB]) { //ตรวจสอบว่าค่าใน regA เท่ากับ regB หรือไม่
                        state.pc += offset; //ถ้าเท่ากัน ให้กระโดดโดยบวก offset เพิ่มเข้าไปใน PC
                    }
                    break;
                }
                case 5: { //คำสั่ง jalr (J-type, opcode = 101)
                    int nextPC = (regA == regB) ? (state.pc + 1) : state.reg[regA]; //กำหนด PC ปลายทาง (หาก regA เท่ากับ regB ให้เป็น pc + 1)
                    state.reg[regB] = state.pc + 1; //เก็บ Return Address (pc + 1) ลงใน regB
                    state.pc = nextPC; //ย้ายตำแหน่ง PC ไปยังปลายทาง
                    break;
                }
                case 6: { //คำสั่ง halt (O-type, opcode = 110)
                    state.pc++; //เพิ่มค่า PC ขึ้น 1
                    halted = true; //ให้ halted เป็น true เพื่อบอกให้หยุดลูปการทำงาน
                    break;
                }
                case 7: { //คำสั่ง noop (O-type, opcode = 111)
                    state.pc++; //เพิ่มค่า PC ขึ้น 1
                    break;
                }
                default: { //กรณีเจอ Opcode ที่ผิด
                    System.err.printf("error: illegal opcode %d\n", opcode);
                    System.exit(1);
                }
            }

            state.reg[0] = 0; //SMC Register=0
        }

        System.out.printf("machine halted\n");
        System.out.printf("total of %d instructions executed\n", instructionsExecuted);
        System.out.printf("final state of machine:\n");

        printState(state);

        return state;
    }

    public static void main(String[] args) {
        simulate("src/Input/Simulator_input.txt");
    }
}
