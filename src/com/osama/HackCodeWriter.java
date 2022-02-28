package com.osama;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class HackCodeWriter {
    private final File file;
    private final PrintWriter wr;
    private SymbolTable table;
    private static HashMap<String, String> compTable = new HashMap<>();

    static {
        // Although it's possible to write an equation solver. It's faster to just hard-code the operations table.
        // Since it only contains 28 operations.

        // operation using the A register(bit 'a' is 0).
        compTable.put("0",   "0101010");
        compTable.put("1",   "0111111");
        compTable.put("-1", "0111010");
        compTable.put("D",   "0001100");
        compTable.put("A",   "0110000");
        compTable.put("!D",  "0001101");
        compTable.put("!A",  "0110001");
        compTable.put("-D",  "0001111");
        compTable.put("-A",  "0110011");
        compTable.put("D+1", "0011111");
        compTable.put("A+1", "0110111");
        compTable.put("D-1", "0001110");
        compTable.put("A-1", "0110010");
        compTable.put("D+A", "0000010");
        compTable.put("D-A", "0010011");
        compTable.put("A-D", "0000111");
        compTable.put("D&A", "0000000");
        compTable.put("D|A", "0010101");

        // operation using the M register(bit 'a' is 1).
        compTable.put("M",   "1110000");
        compTable.put("!M",  "1110001");
        compTable.put("-M",  "1110011");
        compTable.put("M+1", "1110111");
        compTable.put("M-1", "1110010");
        compTable.put("D+M", "1000010");
        compTable.put("D-M", "1010011");
        compTable.put("M-D", "1000111");
        compTable.put("D&M", "1000000");
        compTable.put("D|M", "1010101");
    }

    public HackCodeWriter(String filePath, SymbolTable table){
        PrintWriter sc1;
        File f = new File(filePath);
        this.file = f;

        try {
            sc1 = new PrintWriter(f);
        } catch (FileNotFoundException e) {
            sc1 = null;
            System.out.println("[ERROR] File is either a directory or does not exist.");
            System.exit(1);
        }
        this.wr = sc1;
        this.table = table;
    }

    public void convertCommand(HackParser par){
        String command = null;
        if (par.getCommandType() == Const.A_COMMAND){
            command = convertACommand(par);
        }else if (par.getCommandType() == Const.C_COMMAND){
            command = convertCCommand(par);
        }

        if (command != null){
            this.wr.println(command);
            this.wr.flush();
        }
    }

    private String convertCCommand(HackParser par) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("111");
        commandBuilder.append(HackCodeWriter.compTable.get(par.getComp()));

        int dest = par.getDest();
        for (int i = 1; i <= 4; i = i << 1){
            if ((dest & i) != 0){
                commandBuilder.append("1");
            }else{
                commandBuilder.append("0");
            }
        }

        String jump = Integer.toBinaryString(par.getJump());
        commandBuilder.append("0".repeat(Math.max(0, 3 - jump.length())));
        commandBuilder.append(jump);
        return commandBuilder.toString();
    }

    private String convertACommand(HackParser par){
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("0");

        int value = par.getValue();
        String binValue = Integer.toBinaryString(value);
        commandBuilder.append("0".repeat(Math.max(0, 15 - binValue.length())));
        commandBuilder.append(binValue);

        return commandBuilder.toString();
    }
}