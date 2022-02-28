package com.osama;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;

public class HackParser {
    private final File file;
    private final SymbolTable table;
    private Scanner sc;

    // Address of current command in ROM.
    private int commandAddress = 0;

    // A-type command for A instructions. C-type for C instructions. And L-type for labels(for example (LOOP)).
    private int commandType;

    // symbol and value variables are assigned if the command is A-type.
    private String symbol;
    private int value;

    // jump, dest, and comp are assigned if the command is C-type.
    private int jump;
    private int dest;
    private String comp;

    public HackParser(String filePath, SymbolTable table) throws FileNotFoundException {
        this.file = new File(filePath);
        this.sc = new Scanner(this.file);
        this.table = table;
    }

    public void populateSymbolTable() throws FileNotFoundException {
        List<String> symbols = new ArrayList<>();
        List<Integer> vals = new ArrayList<>();
        int ramAdd = 16;

        while (hasNextCommand()) {
            String line = sc.nextLine();
            line = preproccessCommand(line);

            if (line.length() == 0) continue;
            this.commandType = getTypeOfCommand(line);

            if (this.commandType == Const.A_COMMAND) {
                line = line.substring(1);
                try {
                    Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    symbols.add(line);
                    vals.add(ramAdd++);
                }
                this.commandAddress++;
            } else if (commandType == Const.C_COMMAND){
                this.commandAddress++;
            }else {
                parseLCommand(line);
            }
        }


        int offset = 0;
        for (int i = 0; i < symbols.size(); i++){
            if (this.table.contains(symbols.get(i))){
                offset++;
            }else{
                this.table.addSymbol(symbols.get(i), vals.get(i) - offset);
            }
        }

        this.commandAddress = 0;
        this.sc = new Scanner(this.file);
    }

    public boolean hasNextCommand() {
        return sc.hasNextLine();
    }

    public void advance() {
        if (!hasNextCommand()) {
            throw new IndexOutOfBoundsException(this.file.getName() + " has no more commands to read");
        }

        clearState();

        String line = sc.nextLine();
        line = preproccessCommand(line);

        if (line.length() == 0) return;
        this.commandType = getTypeOfCommand(line);

        if (this.commandType == Const.A_COMMAND) {
            parseACommand(line);
            commandAddress++;
        } else if (this.commandType == Const.C_COMMAND) {
            parseCCommand(line);
            commandAddress++;
        }
    }

    private String preproccessCommand(String line) {
        int idx = line.indexOf("//");

        if (idx != -1) {
            line = line.substring(0, idx);
        }

        line = line.trim();
        return line;
    }

    private void parseLCommand(String line) {
        line = line.substring(1, line.length() - 1);
        this.table.addSymbol(line, this.commandAddress);
    }

    private void parseACommand(String line) {
        line = line.substring(1);
        try {
            this.value = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            this.value = this.table.getAddress(line);
            this.symbol = line;
        }
    }

    private void parseCCommand(String line) {
        String[] splitByDest = line.split("=");
        String rest = splitByDest[0];
        if (splitByDest.length == 2) {
            this.dest = encodeDest(splitByDest[0]);
            rest = splitByDest[1];
        }

        String[] splitByJump = rest.split(";");
        this.comp = splitByJump[0];
        if (splitByJump.length == 2) {
            this.jump = encpdeJumpType(splitByJump[1]);
        } else {
            this.jump = Const.JNULL;
        }
    }

    private int encodeDest(String s) {
        String[] sarr = s.toUpperCase(Locale.ROOT).split("");
        int res = 0;

        for (String item : sarr) {
            if (Objects.equals(item, "A")) res |= Const.ADEST;
            else if (Objects.equals(item, "D")) res |= Const.DDEST;
            else if (Objects.equals(item, "M")) res |= Const.MDEST;
            else {
                throw new IllegalArgumentException("Destination Register: " + item + ", does not exist.");
            }
        }

        return res;
    }

    private int getTypeOfCommand(String line) {
        if (line.charAt(0) == '@') {
            return Const.A_COMMAND;
        } else if (line.charAt(0) == '(' && line.charAt(line.length() - 1) == ')') {
            return Const.L_COMMAND;
        } else if (line.contains(";") || line.contains("=")) {
            return Const.C_COMMAND;
        }

        throw new InvalidParameterException("Invalid command");
    }

    private int encpdeJumpType(String jump) {
        jump = jump.toUpperCase(Locale.ROOT);
        switch (jump) {
            case "JGT":
                return Const.JGT;
            case "JEQ":
                return Const.JEQ;
            case "JGE":
                return Const.JGE;
            case "JLT":
                return Const.JLT;
            case "JNE":
                return Const.JNE;
            case "JLE":
                return Const.JLE;
            case "JMP":
                return Const.JMP;
        }

        return Const.JNULL;
    }

    private void clearState() {
        this.symbol = null;
        this.jump = 0;
        this.comp = null;
        this.commandType = -1;
        this.dest = 0;
        this.value = 0;
    }

    public int getCommandType() {
        return commandType;
    }

    public int getCommandAddress() {
        return commandAddress;
    }

    public int getJump() {
        return jump;
    }

    public int getDest() {
        return dest;
    }

    public String getComp() {
        return comp;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }
}
