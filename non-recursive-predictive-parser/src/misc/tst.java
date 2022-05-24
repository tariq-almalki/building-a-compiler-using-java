package misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class tst {

    private static final ArrayList<String> nonTerminals = new ArrayList<>();

    public static void main(String[] args) {

        // this will match like global for each char       
        Pattern pattern = Pattern.compile(".'{0,2}");
        Matcher matcher = pattern.matcher(("T'→*FT'".replaceFirst(".*(?<=→)(.*)", "$1")));
        List<String> extractedProduction = matcher.results().map(mr -> mr.group()).collect(Collectors.toList());
        Collections.reverse(extractedProduction);
        System.out.println(extractedProduction);
//        String str = "E";
//
//        nonTerminals.add("$");
//        nonTerminals.add("E");
//        nonTerminals.add("T");
//
//        System.out.println(nonTerminals.contains(str));
    }

}
