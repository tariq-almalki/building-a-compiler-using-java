package main.ParseTable;

public class ParseTableDeterminant {

    private String terminal;
    private String production;

    public ParseTableDeterminant(String terminal, String production) {
        this.terminal = terminal;
        this.production = production;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

}
