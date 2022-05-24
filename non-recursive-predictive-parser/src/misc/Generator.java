package misc;

public class Generator {

    private String string = null;
    @SuppressWarnings("FieldMayBeFinal")
    private int i = 0;

    public Generator(String string) {
        this.string = string;
    }

    public String next() {
        return ((Character) string.charAt((i = i == 5 ? 1 : ++i) - 1)).toString();
    }

}
