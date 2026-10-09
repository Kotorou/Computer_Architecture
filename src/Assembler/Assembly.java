package Assembler;

public class Assembly {
    public String label = "";
    public String instruction = "";
    public String field0 = "";
    public String field1 = "";
    public String field2 = "";
    public int address = 0;

    public void printInstruction(){
        System.out.println("label: "+label+"\ninstruction: "+instruction+"\nfield0: "+field0+"\nfield1: "+field1+"\nfield2: "+field2);
    }

}
