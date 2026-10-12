import Assembler.Assembler;
import Simulator.Simulator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {

    //Assembler.AssemblyToMachineCode("src/Input/Test1.txt","src/Output/outtest.txt");
    //Assembler.AssemblyToMachineCode("src/Input/multi.txt","src/Output/Mul_Out.txt");
    //Assembler.AssemblyToMachineCode("src/Input/count.txt","src/Output/countOut.txt");
    //Assembler.AssemblyToMachineCode("src/Input/subroutine.txt","src/Output/subroutineOut.txt");
    //Assembler.AssemblyToMachineCode("src/Input/combi.txt","src/Output/combi_out.txt");
    //Assembler.AssemblyToMachineCode("src/Input/fibo.txt","src/Output/fiboOut.txt");
    Assembler.AssemblyToMachineCode("src/Test/labelunidentified.txt","src/Output/testErrorOutput.txt");
    Assembler.AssemblyToMachineCode("src/Test/duplicatelabel.txt","src/Output/testErrorOutput.txt");
    Assembler.AssemblyToMachineCode("src/Test/numberoutofrange.txt","src/Output/testErrorOutput.txt");
    Assembler.AssemblyToMachineCode("src/Test/instructionnotfound.txt","src/Output/testErrorOutput.txt");
    //Simulator.simulate("src/Output/outtest.txt");
    //Simulator.simulate("src/Output/Mul_Out.txt");
    //Simulator.simulate("src/Output/countOut.txt");
    //Simulator.simulate("src/Output/subroutineOut.txt");
    //Simulator.simulate("src/Output/combi_out.txt");
    //Simulator.simulate("src/Output/fiboOut.txt");
}