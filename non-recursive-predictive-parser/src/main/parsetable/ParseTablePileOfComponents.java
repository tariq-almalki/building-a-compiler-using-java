package main.ParseTable;

import java.util.ArrayList;

public class ParseTablePileOfComponents {

    private String nonTerminal;
    private ArrayList<String> terminals;
    private ArrayList<String> productions;

    public ParseTablePileOfComponents(String nonTerminal, ArrayList<String> terminals, ArrayList<String> productions) {
        this.nonTerminal = nonTerminal;
        this.terminals = terminals;
        this.productions = productions;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public ArrayList<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(ArrayList<String> terminals) {
        this.terminals = terminals;
    }

    public ArrayList<String> getProductions() {
        return productions;
    }

    public void setProductions(ArrayList<String> productions) {
        this.productions = productions;
    }

}
