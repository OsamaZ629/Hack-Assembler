package com.osama;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final static HashMap<String, Integer> predef = new HashMap<>();
    private final HashMap<String, Integer> map;

    public SymbolTable() {
        map = new HashMap<>();
        initPredefinedSymbols();
    }

    public boolean contains(String symbol){
        return this.map.containsKey(symbol);
    }

    public Integer getAddress(String symbol){
        return this.map.get(symbol);
    }

    public void addSymbol(String symbol, int address){
        if(this.map.containsKey(symbol)) return;
        this.map.put(symbol, address);
    }

    private void initPredefinedSymbols(){
        map.put("R0", Const.R0);
        map.put("R1", Const.R1);
        map.put("R2", Const.R2);
        map.put("R3", Const.R3);
        map.put("R4", Const.R4);
        map.put("R5", Const.R5);
        map.put("R6", Const.R6);
        map.put("R7", Const.R7);
        map.put("R8", Const.R8);
        map.put("R9", Const.R9);
        map.put("R10", Const.R10);
        map.put("R11", Const.R11);
        map.put("R12", Const.R12);
        map.put("R13", Const.R13);
        map.put("R14", Const.R14);
        map.put("R15", Const.R15);

        map.put("SP", Const.SP);
        map.put("LCL", Const.LCL);
        map.put("ARG", Const.ARG);
        map.put("THIS", Const.THIS);
        map.put("THAT", Const.THAT);

        map.put("SCREEN", Const.SCREEN);
        map.put("KBD", Const.KBD);
    }
}
