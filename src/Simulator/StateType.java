package Simulator;

public class StateType { //เก็บข้อมูลสถานะ
    public static final int NUMMEMORY = 65536; //หน่วยความจำสูงสุด 65,536 words
    public static final int NUMREGS = 8; //รีจิสเตอร์ 8 ตัว

    public int pc = 0; //Program Counter เก็บแอดเดรสของคำสั่งปัจจุบัน
    public int[] mem = new int[NUMMEMORY]; //หน่วยความจำหลักขนาด 65,536 ช่อง
    public int[] reg = new int[NUMREGS]; //รีจิสเตอร์ 8 ตัว สำหรับเก็บข้อมูลชั่วคราว
    public int numMemory = 0; //เก็บจำนวนบรรทัดของ Machine Code จากไฟล์

    public StateType() { //กำหนดค่าเริ่มต้น
        this.pc = 0; //ให้ PC ชี้ที่คำสั่งแรก
        this.numMemory = 0; //ให้จำนวนบรรทัดคำสั่งเริ่มต้นที่ 0
        for (int i = 0; i < NUMREGS; i++) { //เคลียร์ค่ารีจิสเตอร์
            this.reg[i] = 0; //รีเซ็ตค่าในแต่ละรีจิสเตอร์
        }
        for (int i = 0; i < NUMMEMORY; i++) { //เคลียร์ค่าหน่วยความจำ
            this.mem[i] = 0; //รีเซ็ตค่าในแต่ละช่องของหน่วยความจำ
        }
    }
}
