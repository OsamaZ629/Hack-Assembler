package com.osama;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
	    String filePath = null;
        if (args.length == 0){
            System.out.println("[ERROR] File path must be provided.");
            System.exit(1);
        }else{
            filePath = args[0];
        }

        SymbolTable table = new SymbolTable();
        HackParser par = null;

        try {
            par = new HackParser(filePath, table);
            par.populateSymbolTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String writeFilePath = filePath.split(".asm")[0] + ".hack";
        HackCodeWriter cr = new HackCodeWriter(writeFilePath, table);

        while (par.hasNextCommand()){
            par.advance();
            cr.convertCommand(par);
        }
    }
}
