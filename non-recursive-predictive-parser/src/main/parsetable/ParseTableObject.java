package main.ParseTable;

import java.util.ArrayList;

public class ParseTableObject {
    
    private String nonTerminal;
    private ArrayList<ParseTableDeterminant> determinants;

    public ParseTableObject(String nonTerminal, ArrayList<ParseTableDeterminant> determinant) {
        this.nonTerminal = nonTerminal;
        this.determinants = determinant ;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public ArrayList<ParseTableDeterminant> getDeterminant() {
        return this.determinants;
    }

    public void setDeterminant(ArrayList<ParseTableDeterminant> determinant) {
        this.determinants = determinant;
    }

}
