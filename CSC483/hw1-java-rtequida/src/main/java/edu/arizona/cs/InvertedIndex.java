/*
 * AUTHOR: Ruben Tequida
 * FILE: InvertedIndex.java
 * ASSIGNMENT: Assignment 1
 * COURSE: CSC 583; Fall 2020
 * Purpose: This program takes in a file of documents and corresponding words
 * that are within those files and creates an inverted index from those words.
 * The user can then run a combination of AND/OR queries on the words to determine
 * which documents contain them.
 */

package edu.arizona.cs;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class InvertedIndex {
    boolean indexExists = false;
    String inputFilePath = "";
    HashMap<String, LinkedList> index;

    /*
     * Purpose: Creates an inverted index from the input file passed to it.
     *
     * @param: inputFile, a String indicating the location of the text file
     * that will become the inverted index
     */
    public InvertedIndex(String inputFile) throws FileNotFoundException {
        inputFilePath = inputFile;
        index = new HashMap<>();
        Scanner scanner = null;
        // Attempting to open the given file. Throws exception if can't
        try {
            scanner = new Scanner(new File(inputFile));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        // Look through the file line by line
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ArrayList<String> list = new ArrayList<>(Arrays.asList(line.split(" ")));
            for (int i = 1; i < list.size(); i++) {
                String word = list.get(i);
                // If the dictionary doesn't already contain the word add it and a new
                // linked list as it's value
                if (!(index.containsKey(word))) {
                    index.put(word, new LinkedList<>());
                }
                // If it does contain the word create a new Node and add it to the
                // linked list
                if (!(index.get(word).containsDoc(list.get(0)))) {
                    Node<Integer> node = new Node<>(Integer.parseInt((list.get(0).substring(3))));
                    index.get(word).add(node);
                }
            }
        }
        scanner.close();
    }

    /*
     * Purpose: Initializes the input file, asks the user which method they wish
     * to run and what their query is. Calls the appropriate method and prints out
     * the results
     */
    public static void main(String[] args ) {
        try {
            String fileName = "src/main/resources/input.txt";
            System.out.println("Welcome to Homework 1!");
            Scanner input = new Scanner(System.in);
            System.out.println("Which question do you want to run? (1, 2, 3)");
            String choice = input.nextLine();
            // Get user query
            System.out.println("What is your query?");
            String query = input.nextLine();
            input.close();
            // Create inverted index from input file
            InvertedIndex objInvertedIndex = new InvertedIndex(fileName);
            String[] answer = null;
            // Determine which method to use
            if (choice.equals("1")) {
                answer = objInvertedIndex.runQ5_1(query);
            } else if (choice.equals("2")) {
                answer = objInvertedIndex.runQ5_2(query);
            } else {
                answer = objInvertedIndex.runQ5_3(query);
            }
            // Formatting answer for printing
            String s = "[";
            for (String doc : answer) {
                s += doc + ", ";
            }
            if (s.length() != 1) {
                s = s.substring(0, s.length() - 2);
            }
            s += "]";
            System.out.println(s);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
     * Purpose: Computes the AND operation on two given tokens.
     *
     * @param: query, a String that is a boolean AND statement of two tokens.
     *
     * @return: result, an Array containing all documents that match the query
     */
    public String[] runQ5_1(String query) throws java.io.FileNotFoundException,java.io.IOException {
        String[] tokens = query.split(" ");
        ArrayList<String> resultList = new ArrayList<String>();
        // Setting pointers to head of document lists
        if (index.containsKey(tokens[0]) && index.containsKey(tokens[2])) {
            Node<Integer> node1 = index.get(tokens[0]).getHead();
            Node<Integer> node2 = index.get(tokens[2]).getHead();
            // Search through lists until one or both are finished
            while (node1 != null && node2 != null) {
                // If both lists have the document then add the document to the result
                if (node1.getValue() == node2.getValue()) {
                    String insertDoc = "Doc" + String.valueOf(node1.getValue());
                    resultList.add(insertDoc);
                    // Move both pointers forward
                    node1 = node1.getNext();
                    node2 = node2.getNext();
                    // List 1 needs to progress
                } else if (node1.getValue() < node2.getValue()) {
                    node1 = node1.getNext();
                    // List 2 needs to progress
                } else {
                    node2 = node2.getNext();
                }
            }
        }
        // Adding results to an array
        String[] result = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        return result;
    }

    /*
     * Purpose: Computes the OR operation on two given tokens.
     *
     * @param: query, a String that is a boolean OR statement of two tokens.
     *
     * @return: result, an Array containing all documents that match the query
     */
    public String[] runQ5_2(String query) throws java.io.FileNotFoundException,java.io.IOException {
        String[] tokens = query.split(" ");
        ArrayList<String> resultList = new ArrayList<String>();
        Node<Integer> node1 = null;
        Node<Integer> node2 = null;
        // Setting pointers to head of document lists
        if (index.containsKey(tokens[0])) {
            node1 = index.get(tokens[0]).getHead();
        }
        if (index.containsKey(tokens[2])) {
            node2 = index.get(tokens[2]).getHead();
        }
        // Search through lists until one or both are finished
        while (node1 != null && node2 != null) {
            if (node1.getValue() == node2.getValue()) {
                // If both lists containt he doc add it to result
                String insertDoc = "Doc" + String.valueOf(node1.getValue());
                resultList.add(insertDoc);
                // Move both pointers forward
                node1 = node1.getNext();
                node2 = node2.getNext();
                // Only list 1 contains the doc and is added to result
            } else if (node1.getValue() < node2.getValue()) {
                String insertDoc = "Doc" + String.valueOf(node1.getValue());
                resultList.add(insertDoc);
                // Move list 1 pointer forward
                node1 = node1.getNext();
                // Only list 2 contains the doc and is added to result
            } else {
                String insertDoc = "Doc" + String.valueOf(node2.getValue());
                resultList.add(insertDoc);
                // Move list 2 pointer forward
                node2 = node2.getNext();
            }
        }
        // Continue searching through list 1
        while (node1 != null) {
            String insertDoc = "Doc" + String.valueOf(node1.getValue());
            resultList.add(insertDoc);
            node1 = node1.getNext();
        }
        // Continue searching through list 2
        while (node2 != null) {
            String insertDoc = "Doc" + String.valueOf(node2.getValue());
            resultList.add(insertDoc);
            node2 = node2.getNext();
        }
        // Adding results to an array
        String[] result = new String[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        return result;
    }

    /*
     * Purpose: Computes any given query and computes ANDs first then ORs.
     *
     * @param: query, a String that is a boolean OR statement of two tokens.
     *
     * @return: result, an Array containing all documents that match the query
     */
    public String[] runQ5_3(String query) throws java.io.FileNotFoundException,java.io.IOException {
        // Removing parentheses
        query = query.replace("(", "").replace(")", "");
        // Make a list of query terms
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(query.split(" ")));
        ArrayList<String> resultList = new ArrayList<String>();
        String[] andResult;
        String operator = "AND";
        int i = 1;
        // Go through query and find and compute all ANDs
        while (i < tokens.size()) {
            if (tokens.get(i).equals(operator)) {
                String tempQuery = String.join(" ", tokens.subList(i - 1, i + 2));
                // Process ANDs using method 1
                if (operator.equals("AND")) {
                    andResult = runQ5_1(tempQuery);
                    // Process ANDs using method 2
                } else {
                    andResult = runQ5_2(tempQuery);
                }
                // Adding result of temp query to dictionary
                tempQuery = tempQuery.replace(" ", "");
                addToDict(tempQuery, andResult);
                // Removing processed elements from query
                tokens.remove(i - 1);
                tokens.remove(i - 1);
                tokens.remove(i - 1);
                // Adding newly processed partial query to complete query
                tokens.add(i - 1, tempQuery);
                i--;
            }
            // Switching to OR once entire string has been searched for AND
            if (i == tokens.size() - 1 && operator.equals("AND")) {
                operator = "OR";
                i = 0;
            }
            i++;
        }
        Node current = index.get(query.replace(" ", "")).getHead();
        // Pulling results into a list
        while (current != null) {
            String insertDoc = "Doc" + String.valueOf(current.getValue());
            resultList.add(insertDoc);
            current = current.getNext();
        }
        // Adding results to an array
        String[] result = new String[resultList.size()];
        for (i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        return result;
    }

    /*
     * Purpose: Adds temp queries to the dictionary for later use.
     *
     * @param: key, a String representing the key to be added to the dictionary.
     *
     * @param: elements, an array representing the value to specified key.
     */
    public void addToDict(String key, String[] elements) {
        index.put(key, new LinkedList<>());
        for (String element: elements) {
            Node<Integer> node = new Node<>(Integer.parseInt((element.substring(3))));
            index.get(key).add(node);
        }
    }

}
