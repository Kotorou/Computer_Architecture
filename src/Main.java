import Assembler.Assembler;
import Simulator.Simulator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {

    Assembler.AssemblyToMachineCode("src/Input/Test1.txt","src/Output/outtest.txt");
    Assembler.AssemblyToMachineCode("src/Input/Mul_test.txt","src/Output/Mul_Out.txt");
    Assembler.AssemblyToMachineCode("src/Input/combi.txt","src/Output/combi_out.txt");
    Simulator.simulate("src/Output/combi_out.txt");
}