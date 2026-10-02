package Tools;

public class BinaryTool {



    static public String twoCompliment(String number) {

        int val = Integer.parseInt(number);
        return val > 0 ? 0 + Integer.toBinaryString(val) : Integer.toBinaryString(val);
    }


    static public String signExtension(String binary, int max) {
        int len = binary.length();
        if (len > max) {
            return binary.substring(len - max, len);
        } else if (binary.length() < max) {
            String extension = binary.substring(0, 1);
            StringBuilder binaryBuilder = new StringBuilder(binary);
            for (int i = 0; i < max - len; i++) {
                binaryBuilder.insert(0, extension);
            }
            binary = binaryBuilder.toString();
        }

        return binary;
    }

    static public String unSignExtension(String binary, int max) {
        int len = binary.length();
        if (len > max) {
            return binary.substring(len - max, len);
        } else if (binary.length() < max) {

            StringBuilder binaryBuilder = new StringBuilder(binary);
            for (int i = 0; i < max - len; i++) {
                binaryBuilder.insert(0, "0");
            }
            binary = binaryBuilder.toString();
        }

        return binary;
    }


}
