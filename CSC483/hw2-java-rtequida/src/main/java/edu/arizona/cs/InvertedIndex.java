/*
 * AUTHOR: Ruben Tequida
 * FILE: InvertedIndex.java
 * ASSIGNMENT: Assignment 2
 * COURSE: CSC 583; Fall 2020
 * Purpose: This program takes in a file of documents and corresponding words
 * that are within those files and creates an inverted positional postings index from those words.
 * The user can then run positional queries on the words to determine
 * which documents contain those words within the given value range.
 */

package edu.arizona.cs;
import edu.arizona.cs.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class InvertedIndex {
    boolean indexExists=false;
    String inputFilePath ="";
    HashMap<String, HashMap<Integer, LinkedList<Integer>>> index;

    /*
     * Purpose: Creates an positional inverted index from the input file passed to it.
     *
     * @param: inputFile, a String indicating the location of the text file
     * that will become the inverted index
     */
    public InvertedIndex(String inputFile){
        inputFilePath = inputFile;
        index = new HashMap<String, HashMap<Integer, LinkedList<Integer>>>();
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
            int docNum = Integer.parseInt(list.get(0).substring(3));
            for (int i = 1; i < list.size(); i++) {
                String word = list.get(i);
                // If the dictionary doesn't already contain the word add it and a new
                // dictionary as it's value
                if (!index.containsKey(word)) {
                    index.put(word, new HashMap<Integer, LinkedList<Integer>>());
                }
                // If the words dictionary doesn't already have the current document add it
                // and create a new linked list as its value
                if (!index.get(word).containsKey(docNum)) {
                    index.get(word).put(docNum, new LinkedList<>());
                }
                // If it does contain the word and document create a new Node and add it to the
                // linked list
                Node<Integer> node = new Node<>(i - 1);
                index.get(word).get(docNum).add(node);
            }
        }
        scanner.close();
        indexExists = true;
    }

    /*
     * Purpose: Creates an inverted index from the input file passed to it.
     *
     * @param: inputFile, a String indicating the location of the text file
     * that will become the inverted index
     */
    public static void main(String[] args ) {
        try {
            //a boiler plate main function if you want to test without using mvn test
            //build index
            String fileName = "src/main/resources/Docs.txt";
            System.out.println("********Welcome to  Homework 2!");
            Scanner input = new Scanner(System.in);
            System.out.println("Which question do you want to run?\n\t1:Positional query in both directions\n\t2:Positional query forward only");
            String choice = input.nextLine();
            // Get user query
            System.out.println("What is your query?");
            String query = input.nextLine();
            input.close();
            // Create inverted index from input file
            InvertedIndex objInvertedIndex = new InvertedIndex(fileName);
            Document[] answer = null;
            // Determine which method to use
            if (choice.equals("1")) {
                answer = objInvertedIndex.runQ7_1_1(query);
            } else {
                answer = objInvertedIndex.runQ7_2_directional(query);
            }
            for (Document doc : answer) {
                System.out.println(doc);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
     * Purpose: Finds the documents that both contain the given words and those words
     * are within k words of each other.
     *
     * @param: query, a String that is a boolean AND statement of two tokens.
     *
     * @return: answer, an Array containing all Documents that match the query
     */
    public Document[] runQ7_1_1(String query) throws java.io.FileNotFoundException,ArrayIndexOutOfBoundsException {
        // Check if index is created and create if not
        if (!indexExists) {
            InvertedIndex objInvertedIndex = new InvertedIndex("src/main/resources/Docs.txt");
        }
        ArrayList<Document> result = new ArrayList<>();
        // Split the query into its components
        String[] items = query.split(" ");
        int k = Integer.parseInt(items[1].substring(1));
        // Get the dictionaries associated with the words given in the query
        HashMap<Integer, LinkedList<Integer>> list1 = index.get(items[0]);
        HashMap<Integer, LinkedList<Integer>> list2 = index.get(items[2]);
        // Sort the keysets in order to iterate the documents in order
        ArrayList<Integer> l1Sorted = new ArrayList<>(list1.keySet());
        ArrayList<Integer> l2Sorted = new ArrayList<>(list2.keySet());
        Collections.sort(l1Sorted);
        Collections.sort(l2Sorted);
        int i = 0;
        int j = 0;
        // Search documents containing the given words until one or both are finished
        while (i < l1Sorted.size() && j < l2Sorted.size()) {
            int doc1Num = l1Sorted.get(i);
            int doc2Num = l2Sorted.get(j);
            // If one documents contains both words search for spacing requirements
            if (doc1Num == doc2Num) {
                ArrayList<Integer> l2Pos = new ArrayList<Integer>();
                Node<Integer> l1Pointer = list1.get(doc1Num).getHead();
                Node<Integer> l2Pointer = list2.get(doc2Num).getHead();
                // Move one by one throw first list
                while (l1Pointer != null) {
                    // Compare values in second list to first list
                    while (l2Pointer != null) {
                        // If words are within range k of each other
                        if (Math.abs(l1Pointer.getValue() - l2Pointer.getValue()) <= k) {
                            // Add position of second pointer
                            l2Pos.add(l2Pointer.getValue());
                            // Go to next pointer of first list if beyond the value of the first list pointer
                        } else if (l2Pointer.getValue() > l1Pointer.getValue()) {
                            break;
                        }
                        l2Pointer = l2Pointer.getNext();
                    }
                    // Remove any duplicates they may occur while using precalculated values from previous first list pointer
                    while (!l2Pos.isEmpty() && Math.abs(l2Pos.get(0) - l1Pointer.getValue()) > k) {
                        l2Pos.remove(0);
                    }
                    // Add all remaining results to results list
                    for (Integer pos : l2Pos) {
                        result.add(new Document("Doc" + doc1Num, l1Pointer.getValue(), pos));
                    }
                    l1Pointer = l1Pointer.getNext();
                }
                i++;
                j++;
            } else if (doc1Num < doc2Num) {
                i++;
            } else {
                j++;
            }
        }
        // Adding results to an array
        Document[] answer = new Document[result.size()];
        for (i = 0; i < result.size(); i++) {
            answer[i] = result.get(i);
        }
        return answer;
    }

    /*
     * Purpose: Finds the documents that both contain the given words and those words
     * are within k words of each other.
     *
     * @param: query, a String that is a boolean AND statement of two tokens.
     *
     * @return: answer, an Array containing all Documents that match the query
     */
    public Document[] runQ7_1_2(String query) throws java.io.FileNotFoundException,ArrayIndexOutOfBoundsException {
        // Check if index is created and create if not
        if (!indexExists) {
            InvertedIndex objInvertedIndex = new InvertedIndex("src/main/resources/Docs.txt");
        }
        return runQ7_1_1(query);
    }

    /*
     * Purpose: Finds the documents that both contain the given words and those words
     * are within k words of each other with word1 strictly coming before word2.
     *
     * @param: query, a String that is a boolean AND statement of two tokens.
     *
     * @return: answer, an Array containing all Documents that match the query
     */
    public Document[] runQ7_2_directional(String query) throws java.io.FileNotFoundException,ArrayIndexOutOfBoundsException {
        // Check if index is created and create if not
        if (!indexExists) {
            InvertedIndex objInvertedIndex = new InvertedIndex("src/main/resources/Docs.txt");
        }
        ArrayList<Document> result = new ArrayList<>();
        String[] items = query.split(" ");
        int k = Integer.parseInt(items[1].substring(1));
        // Get the dictionaries associated with the words given in the query
        HashMap<Integer, LinkedList<Integer>> list1 = index.get(items[0]);
        HashMap<Integer, LinkedList<Integer>> list2 = index.get(items[2]);
        // Sorting keyset of word1 dictionary so results are in order
        ArrayList<Integer> l1Sorted = new ArrayList<>(list1.keySet());
        Collections.sort(l1Sorted);
        // Go through each document for word1
        for (Integer doc : l1Sorted) {
            if (list2.containsKey(doc)) {
                Node<Integer> p1 = list1.get(doc).getHead();
                Node<Integer> p2 = list2.get(doc).getHead();
                Node<Integer> hold = null;
                // Continue to search through lists until one hits null
                while (p1 != null && p2 != null) {
                    // Word1 must come first so advance pointer 2
                    if (p2.getValue() < p1.getValue()) {
                        p2 = p2.getNext();
                        // Get positions of word1 and word2 since they are at most k apart
                    } else if ((p2.getValue() - p1.getValue()) <= k) {
                        // Maintain starting position of pointer 2 in order to continue searching with next value of pointer 1
                        if (hold == null) {
                            hold = p2;
                        }
                        result.add(new Document("Doc" + doc, p1.getValue(), p2.getValue()));
                        p2 = p2.getNext();
                        // Reset p2 and advance p1 if p2 gets out of range
                        if (p2 == null) {
                            p2 = hold;
                            hold = null;
                            p1 = p1.getNext();
                        }
                        // Advance p1 if out of range of k
                    } else {
                        if (hold != null) {
                            p2 = hold;
                            hold = null;
                        }
                        p1 = p1.getNext();
                    }
                }
            }
        }
        // Adding results to an array
        Document[] answer = new Document[result.size()];
        for (int i = 0; i < result.size(); i++) {
            answer[i] = result.get(i);
        }
        return answer;
    }

    // toString method for InvertedIndex object for troubleshooting
    public String toString() {
        String s = "";
        for (String word : index.keySet()) {
            s += word + ": ";
            for (Integer docNum : index.get(word).keySet()) {
                s += "[" + docNum + "]: " + index.get(word).get(docNum) + "* ";
            }
            s += "\n";
        }
        s += "\n";
        return s;
    }

}
