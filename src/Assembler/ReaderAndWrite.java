package Assembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ReaderAndWrite {

    public List<String> read(String inputPath) {

        List<String> code = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                code.add(line);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return code;
    }

    public void write(String outputPath, List<String> machineCode) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            for (String code : machineCode) {
                bw.write(code);
                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
