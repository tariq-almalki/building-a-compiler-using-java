package main.slr_table;

public class States {
    
    private String sNumber;
    private Determinant determinant;

    public States(String sNumber, Determinant determinant) {
        this.sNumber = sNumber;
        this.determinant = determinant;
    }

    public String getsNumber() {
        return sNumber;
    }

    public void setsNumber(String sNumber) {
        this.sNumber = sNumber;
    }

    public Determinant getDeterminant() {
        return determinant;
    }

    public void setDeterminant(Determinant determinant) {
        this.determinant = determinant;
    }

}
