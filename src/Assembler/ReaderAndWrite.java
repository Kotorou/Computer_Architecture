package Assembler;



import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class ReaderAndWrite {

    public List<String> read(String inputPath) throws IOException{

        List<String> code = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    code.add(line);
                }
            }

        }

        return code;
    }

    public void write(String outputPath, List<String> machineCode)throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            for (String code : machineCode) {
                bw.write(code);
                bw.newLine();
            }

        }
    }


}
