package main;

// by kiemkist

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import main.ParseTable.ParseTableDeterminant;
import main.ParseTable.ParseTableObject;
import main.ParseTable.ParseTablePileOfComponents;
import main.datastructures.Stack;

public class Parser {

    private static Scanner scanner = null;
    @SuppressWarnings("FieldMayBeFinal")
    private static final Stack stack = new Stack(15);
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final ArrayList<String> expressions = new ArrayList<>();
    private static final ArrayList<ParseTablePileOfComponents> pile = new ArrayList<>();
    private static final ArrayList<ParseTableObject> listOfPTO = new ArrayList<>();
    private static final ArrayList<String> nonTerminals = new ArrayList<>();
    private static final HashMap<String, List<ParseTableDeterminant>> parseTable = new HashMap<>();
    private static final String INPUTS_PATH = "./inputs.txt";
    private static final String PARSE_TABLE_PATH = "./parse-table.txt";

    static class NestedClass {

        private String a = null;

        public NestedClass() {

        }

        public NestedClass(String a) {
            this.a = a;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

    }

    @SuppressWarnings("null")
    public static void main(String[] args) throws FileNotFoundException, Exception {

        try {
            readExpressions(INPUTS_PATH);
            readProductions(PARSE_TABLE_PATH);
            createAListOfParseTableObject();
            createHashMapOfParseTable();
            parsingExpressions();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void readExpressions(String path) throws FileNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("expressions file doesn't exist");
            System.exit(0);
        }

        scanner = new Scanner(file);

        // you need to escape dollar sign to be considered literally as a dollar sign,
        // because it's considered as the end of a string in context of regular expressions.
        // useDelimiter can accept Pattern object | Pattern.compile("\\$") does return Pattern object
        scanner.useDelimiter("\\$");

        // reading the expressions from the file and adding it to the list
        while (scanner.hasNext()) {
            // next and nextLine doesn't consume newLine character.
            expressions.add(scanner.next() + "$");
            scanner.nextLine();
        }

    }

    // return ParseTableObject
    private static void readProductions(String path) throws FileNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("productions file doesn't exist");
            System.exit(0);
        }

        scanner = new Scanner(file);

        String firstRow = scanner.nextLine().trim();
        List<String> terminals = Arrays.asList(firstRow.replaceAll("^_*|_*$", "").replaceAll("_+", ",").split(","));

        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String nonTerminal = row.replaceAll("(^[A-Z']{1,4})(?=[ ]\\||\\|).*", "$1");
            nonTerminals.add(nonTerminal);
            List<String> productions = Arrays.asList(row.replaceAll(".*?(?<=\\|[ ])([^\\|]+)(?=\\b).*", "$1").replace(", ", ",").split(","));
            pile.add(new ParseTablePileOfComponents(nonTerminal, new ArrayList<>(terminals), new ArrayList<>(productions)));
            scanner.nextLine();
        }
    }

    private static void createAListOfParseTableObject() {
        // [P]arse[T]able[O]bject  --> PTO
        // every componenet in ParseTablePileOfComponenets correspond to one ParseTableObject. 

        pile.stream().forEach((ParseTablePileOfComponents componenet) -> {
            ArrayList<ParseTableDeterminant> determinants = new ArrayList<>();
            // using getTerminals().size will not differ from using getProductions().size() 
            for (int index = 0; index < componenet.getTerminals().size(); index++) {
                determinants.add(new ParseTableDeterminant(componenet.getTerminals().get(index), componenet.getProductions().get(index)));
            }

            listOfPTO.add(new ParseTableObject(componenet.getNonTerminal(), determinants));
        });

    }

    private static void createHashMapOfParseTable() {
        listOfPTO.stream().forEach((ParseTableObject PTO) -> {
            parseTable.put(PTO.getNonTerminal(), PTO.getDeterminant());
        });
    }

    private static void parsingExpressions() {
        for (String expression : expressions) {
            System.out.println("Expression: " + expression.substring(0, expression.length() - 2));
            parsingExpression(expression, new Scanner(expression));
            stack.clear();
            System.out.println("\n");
        }
    }

    private static void parsingExpression(String expression, Scanner scanner) {

        String matchedStr = "";
        AtomicInteger skip = new AtomicInteger(0);
        @SuppressWarnings("MismatchedReadAndWriteOfArray")
        String[] arrForExpression = expression.split(" ");
        int length = arrForExpression.length;

        try {
            System.out.println("|" + "-".repeat((length * 6) + 22 + 7 + 13 - 3) + "|");
            System.out.printf("|%-" + (length - 3) + "s %-" + (length + 22) + "s %-" + (length + 7) + "s %-" + (length * 2 + 3) + "s %-" + (length + 6) + "s|%n", "", "Matched", "Stack", "Input", "Action");
            System.out.println("|" + "-".repeat((length * 6) + 22 + 7 + 13 - 3) + "|");

            // pushing initial values.
            stack.push("$");
            stack.push("E");

            NestedClass nc = new NestedClass();

            // let a be the first symbol of expression
            nc.setA(scanner.next());
            // let X be the top stack symbol
            String X = stack.peek();
            while (!stack.peek().equals("$")) {
                if (X.equals(nc.getA())) {
                    matchedStr += " " + nc.getA();
                    System.out.printf("|%-" + (length - 3) + "s %-" + (length + 22) + "s %-" + (length + 7) + "s %-" + (length * 2 + 3) + "s %-" + (length + 6) + "s|%n", "", matchedStr, stack.printStack(), Stream
                            .of(arrForExpression)
                            .skip(skip.getAndIncrement())
                            .collect(Collectors.joining(" ")), "matched " + nc.getA());
                    nc.setA(scanner.next());
                    stack.pop();
                } else if (!nonTerminals.contains(X)) {
                    error(matchedStr, stack.printStack(), Stream
                            .of(arrForExpression)
                            .skip(skip.get())
                            .collect(Collectors.joining(" ")), "Syntax Error", length);
                    break;
                } else if (parseTable.get(X).stream().anyMatch(PTD -> PTD.getTerminal().equals(nc.getA()) && PTD.getProduction().equals("blank"))) {
                    error(matchedStr, stack.printStack(), Stream
                            .of(arrForExpression)
                            .skip(skip.get())
                            .collect(Collectors.joining(" ")), "Syntax Error", length);
                    break;
                } else if (parseTable.get(X).stream().anyMatch(PTD -> PTD.getTerminal().equals(nc.getA()) && !PTD.getProduction().equals("blank"))) {

                    String action = parseTable
                            .get(X)
                            .stream()
                            .filter(PTD -> PTD.getTerminal().equals(nc.getA()) && !PTD.getProduction().equals("blank"))
                            .findFirst()
                            .get()
                            .getProduction();

                    System.out.printf("|%-" + (length - 3) + "s %-" + (length + 22) + "s %-" + (length + 7) + "s %-" + (length * 2 + 3) + "s %-" + (length + 6) + "s|%n", "", matchedStr, stack.printStack(), Stream
                            .of(arrForExpression)
                            .skip(skip.get())
                            .collect(Collectors.joining(" ")), action);

                    stack.pop();

                    for (String TornonT : RHS(action.replaceFirst(".*(?<=→)(.*)", "$1"))) {
                        stack.push(TornonT);
                    }

                } else {
                    error(matchedStr, stack.printStack(), Stream
                            .of(arrForExpression)
                            .skip(skip.get())
                            .collect(Collectors.joining(" ")), "Syntax Error", length);
                    break;
                }
                X = stack.peek();
            }

            if (X.equals(nc.getA())) {
                System.out.printf("|%-" + (length - 3) + "s %-" + (length + 22) + "s %-" + (length + 7) + "s %-" + (length * 2 + 3) + "s %-" + (length + 6) + "s|%n", "", matchedStr, stack.printStack(), Stream
                        .of(arrForExpression)
                        .skip(skip.get())
                        .collect(Collectors.joining(" ")), "successfully parsed");
            }

            System.out.println("|" + "-".repeat(((length * 6) + 22 + 7 + 13 - 3)) + "|");
        } catch (Exception e) {

        }

    }

    private static ArrayList<String> RHS(String rhs) {
        ArrayList<String> rhsList = new ArrayList<>();
        switch (rhs) {
            case "TE'":
                rhsList.add("E'");
                rhsList.add("T");
                break;
            case "ε":
                break;
            case "FT'":
                rhsList.add("T'");
                rhsList.add("F");
                break;
            case "+TE'":
                rhsList.add("E'");
                rhsList.add("T");
                rhsList.add("+");
                break;
            case "*FT'":
                rhsList.add("T'");
                rhsList.add("F");
                rhsList.add("*");
                break;
            case "id":
                rhsList.add("id");
                break;
            case "(E)":
                rhsList.add(")");
                rhsList.add("E");
                rhsList.add("(");
                break;
        }

        return rhsList;
    }

    private static void error(String matched, String Stack, String Input, String action, int length) {
        System.out.printf("|%-" + (length - 3) + "s %-" + (length + 12) + "s %-" + (length + 7) + "s %-" + (length + 15) + "s %-" + (length + 6) + "s|%n", "", matched, Stack, Input, action);
    }

}
