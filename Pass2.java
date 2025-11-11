import java.io.*;
import java.util.*;

public class Pass2 {
    public static void main(String[] args) throws IOException {
        BufferedReader ic = new BufferedReader(new FileReader("intermediate.txt")); // your ic.txt renamed
        BufferedReader sym = new BufferedReader(new FileReader("symtab.txt"));
        BufferedReader lit = new BufferedReader(new FileReader("littab.txt"));
        FileWriter out = new FileWriter("Pass2.txt");

        HashMap<Integer, String> symtab = new HashMap<>();
        HashMap<Integer, String> littab = new HashMap<>();

        String line;
        int index = 1;

        // Reading symbol table
        while ((line = sym.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            if (parts.length >= 2)
                symtab.put(index++, parts[1]);
        }

        index = 1;
        // Reading literal table
        while ((line = lit.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            if (parts.length >= 2)
                littab.put(index++, parts[1]);
        }

        // Reading intermediate code
        while ((line = ic.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                out.write("\n");
                continue;
            }

            if (line.contains("(IS,")) {
                String opcode = line.substring(line.indexOf("(IS,") + 4, line.indexOf("(IS,") + 6);
                String reg = "0";
                String operand = "000";

                if (line.contains("(S,")) {
                    int sIndex = Integer.parseInt(line.substring(line.indexOf("(S,") + 3, line.indexOf(")", line.indexOf("(S,"))));
                    operand = symtab.getOrDefault(sIndex, "000");
                } else if (line.contains("(L,")) {
                    int lIndex = Integer.parseInt(line.substring(line.indexOf("(L,") + 3, line.indexOf(")", line.indexOf("(L,"))));
                    operand = littab.getOrDefault(lIndex, "000");
                } else if (line.contains("(C,")) {
                    operand = line.substring(line.indexOf("(C,") + 3, line.indexOf(")", line.indexOf("(C,")));
                    operand = String.format("%03d", Integer.parseInt(operand));
                }

                out.write("+ " + opcode + " " + reg + " " + operand + "\n");
            }
            else if (line.contains("(DL,01)")) {
                // DC
                String val = line.substring(line.indexOf("(C,") + 3, line.indexOf(")", line.indexOf("(C,")));
                val = String.format("%03d", Integer.parseInt(val));
                out.write("+ 00 0 " + val + "\n");
            }
            else if (line.contains("(DL,02)")) {
                // DS — reserve, no direct machine code
                out.write("+ 00 0 000\n");
            }
            else if (line.contains("(IS,00)")) {
                // STOP
                out.write("+ 00 0 000\n");
            }
            else {
                out.write("\n");
            }
        }

        ic.close();
        sym.close();
        lit.close();
        out.close();
    }
}
