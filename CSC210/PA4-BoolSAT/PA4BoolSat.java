
/* AUTHOR: Ruben Tequida
 * FILE: PA4BoolSat.java
 * ASSIGNMENT: Programming Assignment 4 - BoolSAT
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program takes in an array of strings where the first element
 * is the filename and the second element is an optional command of DEBUG. The
 * given file will contain a boolean equation of the proper format. If the
 * program is not given the DEBUG command it will create an AST of the
 * equation, determine if any combination of variable conditions will
 * evaluate to true and print SAT and the combinations that made it true or it
 * will print UNSAT. If the DEBUG command is given it will print SAT if the 
 * equation every evaluates to true and UNSAT otherwise, but it will always
 * print every true/false combination.
 * 
 * Example input file:
 * 
 * a NAND (b NAND $t) NAND _3
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import bool_exp.ASTNode;
import bool_exp.BoolSatParser;

public class PA4BoolSat {
    /*
     * Purpose: Calls functions to read in the file, create an AST from the
     * given equation, evaluates every possible true/false combination for
     * each variable, and print out the desired results.
     * 
     * @param args, which takes in arguments from the command line in the
     * order filename, DEBUG command (optional).
     * 
     * @return None.
     */
    public static void main(String[] params) {
        ASTNode root = createAST(params[0]);
        TreeMap<String, Boolean> idMap = getIds(root,
                new TreeMap<String, Boolean>());
        TreeMap<String, Boolean> allResults = new TreeMap<String, Boolean>();
        getChoices(new ArrayList<Boolean>(), idMap.size(), root, idMap,
                allResults);
        printResults(allResults, params);
    }

    /*
     * Purpose: Opens the file and creates an ASTNode from the given equation.
     * 
     * @param file, which is a string of the filename given.
     * 
     * @return an ASTNode, which is a parsed version of the given equation.
     */
    public static ASTNode createAST(String file) {
        Scanner fileName = null;
        // Attempts to open the given file.
        try {
            fileName = new Scanner(new File(file));
            // Throws an exception if the file can't be found.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        String expression = fileName.nextLine();
        System.out.println("input: " + expression);
        return BoolSatParser.parse(expression);
    }

    /*
     * Purpose: Traverses the AST and gets each unique variable in the given
     * equation and sets their value to false.
     * 
     * @param node, which is an ASTNode of the given equation. idMap, which is
     * a map with Strings as keys and Booleans as values.
     * 
     * @return idMap, which holds the equations variables as keys and their
     * boolean values which are initialized to false.
     */
    public static TreeMap<String, Boolean> getIds(ASTNode node,
            TreeMap<String, Boolean> idMap) {
        if (node.isId()) {
            idMap.put(node.getId(), false);
        } else if (node.isNand()) {
            getIds(node.child1, idMap);
            getIds(node.child2, idMap);
        }
        return idMap;
    }

    /*
     * Purpose: Creates every possibility of true/false combinations for each
     * variable.
     * 
     * @param soFar is a list of boolean, i is an integer to keep track of the
     * idMap, root is the root of the AST, idMap is map of all the variables in
     * the equation, and allResults is a map of String to Boolean for every
     * true/false combination.
     * 
     * @return None.
     */
    public static void getChoices(ArrayList<Boolean> soFar, int i, ASTNode root,
            TreeMap<String, Boolean> idMap,
            TreeMap<String, Boolean> allResults) {
        // Base case: soFar is the size of the number of variables given.
        if (soFar.size() == i) {
            addChoiceToMap(soFar, root, idMap, allResults);
            // Chooses and unchooses true/false and recurses on new variable.
        } else {
            soFar.add(false);
            getChoices(soFar, i, root, idMap, allResults);
            soFar.remove(soFar.size() - 1);
            soFar.add(true);
            getChoices(soFar, i, root, idMap, allResults);
            soFar.remove(soFar.size() - 1);
        }
    }

    /*
     * Purpose: Adds the given choice to allResults and evaluates the outcome
     * of those choices in the given equation.
     * 
     * @param choice is a list of booleans, node is a node within the AST that
     * is being evaluated, idMap is map of all the variables in the equation,
     * and allResults is a map of String to Boolean for every true/false
     * combination.
     * 
     * @return None.
     */
    public static void addChoiceToMap(ArrayList<Boolean> choice, ASTNode node,
            TreeMap<String, Boolean> idMap,
            TreeMap<String, Boolean> allResults) {
        int i = 0;
        String temp = "";
        // Assigns each variable in idMap to its corresponding boolean choice.
        for (String key : idMap.keySet()) {
            idMap.put(key, choice.get(i));
            temp += key + ": " + choice.get(i) + ", ";
            i++;
        }
        ArrayList<Boolean> outcome = new ArrayList<Boolean>();
        evaluateChoice(node, idMap, outcome);
        // Manipulates temp so it produces desired print value.
        temp = temp.substring(0, temp.length() - 2);
        allResults.put(temp, outcome.get(0));
    }

    /*
     * Purpose: Evaluates the chosen combination of true/false and adds the
     * outcome to soFar.
     * 
     * @param node is an ASTNode and is the position currently being
     * evaluated, idMap is map of all the variables in the equation, and soFar
     * is a list containing the result of the evaluated equation.
     * 
     * @return None.
     */
    public static void evaluateChoice(ASTNode node,
            TreeMap<String, Boolean> idMap, ArrayList<Boolean> soFar) {
        // Base case: add the boolean value to soFar when Id node is reached.
        if (node.isId()) {
            soFar.add(idMap.get(node.getId()));
        } else {
            // Searches through the children first.
            evaluateChoice(node.child1, idMap, soFar);
            evaluateChoice(node.child2, idMap, soFar);
            // Removes the last two elements of soFar and evaluates them.
            boolean val1 = soFar.remove(soFar.size() - 1);
            boolean val2 = soFar.remove(soFar.size() - 1);
            soFar.add(!(val1 && val2));
        }
    }

    /*
     * Purpose: If DEBUG wasn't given it prints out if the equation was SAT or
     * UNSAT and every true/false combination that made it true. If DEBUG was
     * selected it prints out if the equation was SAT or UNSAT and every true/
     * false combination and then the outcome of that combination.
     * 
     * @param allResutls is a map of Strings to Booleans and holds every true/
     * false combination and the result of that combination. params is an
     * array of Strings that holds the filename and whether DEBUG is chosen or
     * not.
     * 
     * @return None.
     */
    public static void printResults(TreeMap<String, Boolean> allResults,
            String[] params) {
        String found = "UNSAT";
        // Determines if the equation is SAT by finding true in the map values.
        for (String key : allResults.keySet()) {
            if (allResults.get(key) == true) {
                found = "SAT";
            }
        }
        // Prints the desired outcome whether DEBUG was selected or not.
        System.out.println(found);
        for (String key : allResults.keySet()) {
            if (params.length == 2 && params[1].contentEquals("DEBUG")) {
                System.out.println(key + ", " + allResults.get(key));
            } else {
                if (allResults.get(key) == true) {
                    System.out.println(key);
                }
            }
        }
    }
}
