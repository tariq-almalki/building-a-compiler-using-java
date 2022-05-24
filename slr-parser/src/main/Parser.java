package main;

// by kiemkist

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import main.datastructures.Stack;
import main.slr_table.Determinant;

public class Parser {

    private static Scanner scanner = null;
    private static final Stack stack_stack = new Stack(15);
    private static final Stack stack_symbols = new Stack(15);
    private static final String EXPRESSIONS_PATH = "./expressions.txt";
    private static final String SLR_PARSE_TABLE_PATH = "./slr-table.txt";
    private static final String SLR_PRODUCTIONS_PATH = "./productions.txt";
    private static final HashMap<String, Determinant> slrTable = new HashMap<>();
    private static final HashMap<String, String> slrProductions = new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final ArrayList<String> expressions = new ArrayList<>();

    static class NestedClass {

        private String a = null;

        public NestedClass() {

        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

    }

    public static void main(String[] args) {

        try {
            readExpressions(EXPRESSIONS_PATH);
            readSlrProductions(SLR_PRODUCTIONS_PATH);
            readSlrTable(SLR_PARSE_TABLE_PATH);
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

    private static void readSlrTable(String path) throws FileNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("expressions file doesn't exist");
            System.exit(0);
        }

        scanner = new Scanner(file);
        String fRow = scanner.nextLine().trim();
        List<String> terminals = Arrays.asList(fRow
                .replaceAll("(.+)(?=[|]).+", "$1")
                .replaceAll("^_*|_*$", "")
                .replaceAll("_+", ",").split(","));
        List<String> nonTerminals = Arrays.asList(fRow
                .replaceAll(".+(?<=[|])(.+)", "$1")
                .replaceAll("^_*|_*$", "")
                .replaceAll("_+", ",").split(","));

        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            @SuppressWarnings("MismatchedReadAndWriteOfArray")
            String[] rowParts = row.replaceAll("(.+)(?=[|]).+", "$1").split("[|]");
            String sNumber = rowParts[0].trim();
            List<String> sActions = Arrays.asList(rowParts[1].trim().replaceAll("\\s+", "").split(","));
            List<String> sGotos = Arrays.asList(rowParts[2].trim().replaceAll("\\s+", "").split(","));

            HashMap<String, String> sAction = new HashMap<>();
            HashMap<String, String> sGoto = new HashMap<>();

            Iterator<String> iteratorOfTerminals = terminals.iterator();
            Iterator<String> iteratorOfActions = sActions.iterator();

            while (iteratorOfTerminals.hasNext() && iteratorOfActions.hasNext()) {
                sAction.put(iteratorOfTerminals.next(), iteratorOfActions.next());
            }

            Iterator<String> iteratorOfNonTerminals = nonTerminals.iterator();
            Iterator<String> iteratorOfGotos = sGotos.iterator();

            while (iteratorOfNonTerminals.hasNext() && iteratorOfGotos.hasNext()) {
                sGoto.put(iteratorOfNonTerminals.next(), iteratorOfGotos.next());
            }

            slrTable.put(sNumber, new Determinant(sAction, sGoto));

            scanner.nextLine();
        }

    }

    private static void readSlrProductions(String path) throws FileNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("expressions file doesn't exist");
            System.exit(0);
        }

        scanner = new Scanner(file);

        AtomicInteger nProduction = new AtomicInteger(1);

        while (scanner.hasNextLine()) {
            String production = scanner.nextLine();
            slrProductions.put(nProduction.getAndIncrement() + "", production.trim());

        }
    }

    private static void parsingExpressions() {
        for (String expression : expressions) {
            System.out.println("Expression: " + expression.substring(0, expression.length() - 2));
            parsingExpression(expression, new Scanner(expression));
            stack_stack.clear();
            stack_symbols.clear();
            System.out.println("\n");
        }
    }

    private static void parsingExpression(String expression, Scanner scanner) {

        AtomicInteger skip = new AtomicInteger(0);
        AtomicInteger line = new AtomicInteger(1);
        @SuppressWarnings("MismatchedReadAndWriteOfArray")
        String[] arrOfExpressions = expression.split(" ");

        try {

            System.out.println("|" + "-".repeat(94) + "|");
            System.out.printf("|%-5s| %-20s| %-17s| %-23s| %-21s|%n", "Line", "Stack", "Symbols", "Input", "Action");
            System.out.println("|" + "-".repeat(94) + "|");

//            pushing the first state
            stack_stack.push("0");
//            pushing dollar sign in stack_symbol
            stack_symbols.push("$");

            NestedClass nc = new NestedClass();
            nc.setA(scanner.next());

            while (true) {
                String s = stack_stack.peek();

                String action = slrTable.get(s).getsAction().get(nc.getA());

                if (action.matches("^s\\d+")) {

//                 extract the new state from the action
                    String sNumber = action.substring(1);

//                 print statement
                    System.out.printf("|%-5s| %-20s| %-17s| %-23s| %-21s|%n",
                            "("+line.getAndIncrement()+")",
                            stack_stack.printStack().trim(),
                            stack_symbols.printStack(),
                            Stream
                                    .of(arrOfExpressions)
                                    .skip(skip.getAndIncrement())
                                    .collect(Collectors.joining(" ")),
                            "shift to " + sNumber);

//                 pushing the shifted symbol   
                    stack_symbols.push(nc.getA());
                    
//                 pushing the new state
                    stack_stack.push(sNumber);

                    nc.setA(scanner.next());

                } else if (action.matches("^r\\d+")) {

//                 extract the new state from the action
                    String sNumber = action.substring(1);

//                 reducing production
                    String production = slrProductions.get(sNumber);

//                 print statement
                    System.out.printf("|%-5s| %-20s| %-17s| %-23s| %-21s|%n",
                            "("+line.getAndIncrement()+")",
                            stack_stack.printStack().trim(),
                            stack_symbols.printStack(),
                            Stream
                                    .of(arrOfExpressions)
                                    .skip(skip.get())
                                    .collect(Collectors.joining(" ")),
                            "reduce by " + production);

                    String leftHandSide = production.replaceAll("(.*)(?=[ ]→).*", "$1").trim();
                    String rightHandSide = production.replaceAll(".*(?<=→[ ])(.*)", "$1").trim();

                    @SuppressWarnings("MismatchedReadAndWriteOfArray")
                    String[] arrOfRHS = rightHandSide.split(" ");

                    for (String symbol : arrOfRHS) {
                        stack_symbols.pop();
                        stack_stack.pop();
                    }
                    
                    s = stack_stack.peek();

                    stack_symbols.push(leftHandSide);

                    String gotoState = slrTable.get(s).getsGoto().get(leftHandSide);
                    
                    if(!gotoState.equals("blank")){                        
                    stack_stack.push(gotoState);
                    }

                } else if (action.equals("acc")) {
//                 print statement
                    System.out.printf("|%-5s| %-20s| %-17s| %-23s| %-21s|%n",
                            "("+line.getAndIncrement()+")",
                            stack_stack.printStack().trim(),
                            stack_symbols.printStack(),
                            Stream
                                    .of(arrOfExpressions)
                                    .skip(skip.get())
                                    .collect(Collectors.joining(" ")),
                            "accept");
                    break;

                } else {
                    error(line, skip, stack_stack, stack_symbols, arrOfExpressions, "Syntax Error");
                    break;
                }

            }
            
            System.out.println("|" + "-".repeat(94) + "|");
        } catch (Exception e) {
        }

    }

    private static void error(AtomicInteger line, AtomicInteger skip, Stack stack_stack, Stack stack_symbols, String[] arrOfExpressions, String error) {
//                 print statement
                    System.out.printf("|%-5s| %-20s| %-17s| %-23s| %-21s|%n",
                            "("+line.getAndIncrement()+")",
                            stack_stack.printStack(),
                            stack_symbols.printStack(),
                            Stream
                                    .of(arrOfExpressions)
                                    .skip(skip.get())
                                    .collect(Collectors.joining(" ")),
                            error);
    }

}
