package main.slr_table;

import java.util.HashMap;

public class Determinant {
    
//          key: terminal, value: action
    private HashMap<String, String> sAction;
//          key: non-terminal value: gotoNumber
    private HashMap<String, String> sGoto;

    public Determinant(HashMap<String, String> sAction, HashMap<String, String> sGoto) {
        this.sAction = sAction;
        this.sGoto = sGoto;
    }

    public HashMap<String, String> getsAction() {
        return sAction;
    }

    public void setsAction(HashMap<String, String> sAction) {
        this.sAction = sAction;
    }

    public HashMap<String, String> getsGoto() {
        return sGoto;
    }

    public void setsGoto(HashMap<String, String> sGoto) {
        this.sGoto = sGoto;
    }
   
}
