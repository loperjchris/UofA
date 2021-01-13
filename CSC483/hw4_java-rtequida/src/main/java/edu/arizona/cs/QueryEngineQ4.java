/*
 * AUTHOR: Ruben Tequida
 * FILE: QueryEngineQ4.java
 * ASSIGNMENT: Assignment 4
 * COURSE: CSC 583; Fall 2020
 * Purpose: This program takes in a file of documents and corresponding words
 * that are within those files and creates an inverted postings index from those words.
 * The user can then run queries on the words to determine
 * which documents have the highest relevance score.
 */

package edu.arizona.cs;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.simple.Token;

public class QueryEngineQ4 {
    //Global Variables
    String inputFile = "";
    HashMap<Integer, HashMap<String, Integer>> index;
    HashMap<String, Integer> totals;
    HashMap<Integer, Integer> docLenTotals;
    int totalTokens;

    // Class constructor: Initializes variables and calls function to build index
    public QueryEngineQ4(String inputFile){
        this.inputFile = inputFile;
        index = new HashMap<>();
        totals = new HashMap<>();
        docLenTotals = new HashMap<>();
        totalTokens = 0;
        buildModel();
    }

    /*
     * Purpose: Creates an inverted index from the input file passed to it.
     */
    private void buildModel() {
        // Path to input file
        String inputPath = "src/main/resources/" + inputFile;
        Scanner scanner = null;
        try {
            // Opening given file
            scanner = new Scanner(new File(inputPath));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        // Continue reading file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Get document number
            int docid = Integer.parseInt(line.substring(3,4));
            // Using a temporary dictionary to get word count
            HashMap<String, Integer> tempdict = new HashMap<String, Integer>();
            String data = line.substring(5);
            StringBuilder tokens = new StringBuilder();
            StringBuilder result = new StringBuilder();
            // Tokenize each term
            for (Token token: new Sentence(data.toLowerCase()).tokens()) {
                tokens.append(token.word() + " ");
            }
            // Lemmatize each token
            for (String lemma : new Sentence(tokens.toString().toLowerCase()).lemmas()) {
                result.append(lemma + " ");
            }
            // Removing punctuation from tokens
            ArrayList<String> list = new ArrayList<>(Arrays.asList(result.toString().replaceAll("[^a-zA-Z ]", "").split(" ")));
            // Adding each word to the dictionary and keeping count of their occurrences in the doc and in the context
            for (int i = 0; i < list.size(); i++) {
                String word = list.get(i);
                if (tempdict.containsKey(word)) {
                    tempdict.put(word, tempdict.get(word) + 1);
                } else {
                    tempdict.put(word, 1);
                }
                if (totals.containsKey(word)) {
                    totals.put(word, totals.get(word) + 1);
                } else {
                    totals.put(word, 1);
                }
                if (docLenTotals.containsKey(docid)) {
                    docLenTotals.put(docid, docLenTotals.get(docid) + 1);
                } else {
                    docLenTotals.put(docid, 1);
                }
                totalTokens++;
            }
            index.put(docid, tempdict);
        }
        scanner.close();
    }

    /*
     * Purpose: Calls the method to create the index and prints the results.
     */
    public static void main(String[] args ) {
        try {
            String inputFile = "input.txt";
            QueryEngineQ4 qe = new QueryEngineQ4(inputFile);
            String query[] = new String[]{"information", "retrieval"};
            List<ResultClass> ans = qe.runQ4_3_with_smoothing(query);
            for (ResultClass rc : ans) {
                System.out.println(rc.DocName.docid + " " + rc.docScore);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
     * Purpose: Runs the Jelinek-Mercer language model with smoothing to rank docs.
     *
     * @param: query, a string array representing the user's query
     */
    public List<ResultClass> runQ4_3_with_smoothing(String[] query) throws java.io.FileNotFoundException {
        HashMap<Integer, Double> tally = new HashMap<>();
        int termFreq;
        int termInContext;
        double val;
        StringBuilder tokens = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String temp = "";
        for (String word : query) {
            temp += word + " ";
        }
        // Tokenize the query
        for (Token token: new Sentence(temp.toLowerCase()).tokens()) {
            tokens.append(token.word() + " ");
        }
        // Lemmatize the query
        for (String lemma : new Sentence(tokens.toString().toLowerCase()).lemmas()) {
            result.append(lemma + " ");
        }
        ArrayList<String> list = new ArrayList<>(Arrays.asList(result.toString().split(" ")));
        // For each word in query get it's frequency in the doc and in the context
        for (String word : list) {
            for (Integer doc : index.keySet()) {
                termFreq = 0;
                termInContext = 0;
                if (index.get(doc).containsKey(word)) {
                    termFreq = index.get(doc).get(word);
                }
                if (totals.containsKey(word)) {
                    termInContext = totals.get(word);
                }
                // Jelinek-Mercer language model
                val = (0.5 * (Double.valueOf(termFreq) / Double.valueOf(docLenTotals.get(doc)))) + (0.5 * (Double.valueOf(termInContext) / Double.valueOf(totalTokens)));
                // Multiply each result for a given doc to get the total ranking for multi-term queries
                if (tally.containsKey(doc)) {
                    tally.put(doc, tally.get(doc) * val);
                } else {
                    tally.put(doc, val);
                }
            }
        }
        // Sorting the hashmap by value
        ArrayList<Map.Entry<Integer, Double>> sorter = new ArrayList<>();
        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> e: tally.entrySet()) {
            sorter.add(e);
        }
        // Creating a new comparator to determine how to compare values to each other
        Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> e1, Map.Entry<Integer, Double> e2) {
                Double d1 = e1.getValue();
                Double d2 = e2.getValue();
                return d2.compareTo(d1);
            }
        };
        // Adding values back to map in sorted order
        Collections.sort(sorter, valueComparator);
        for (Map.Entry<Integer, Double> e : sorter) {
            sortedMap.put(e.getKey(), e.getValue());
        }
        System.out.println(sortedMap);
        // Adding ranked docs and their scores to the ans array to be returned
        List<ResultClass> ans = new ArrayList<ResultClass>();
        for (Integer docID : sortedMap.keySet()) {
            Document doc = new Document("Doc" + docID);
            ResultClass rc = new ResultClass();
            rc.DocName = doc;
            rc.docScore = sortedMap.get(docID);
            ans.add(rc);
        }
        return ans;
    }

    /*
     * Purpose: Runs the Jelinek-Mercer language model without smoothing to rank docs.
     *
     * @param: query, a string array representing the user's query
     */
    public List<ResultClass> runQ4_3_without_smoothing(String[] query) throws java.io.FileNotFoundException {
        HashMap<Integer, Double> tally = new HashMap<>();
        int termFreq;
        int termInContext;
        double val;
        StringBuilder tokens = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String temp = "";
        for (String word : query) {
            temp += word + " ";
        }
        // Tokenize query
        for (Token token: new Sentence(temp.toLowerCase()).tokens()) {
            tokens.append(token.word() + " ");
        }
        // Lemmatize query
        for (String lemma : new Sentence(tokens.toString().toLowerCase()).lemmas()) {
            result.append(lemma + " ");
        }
        // For each word in query get it's frequency in the doc and in the context
        ArrayList<String> list = new ArrayList<>(Arrays.asList(result.toString().split(" ")));
        for (String word : list) {
            for (Integer doc : index.keySet()) {
                termFreq = 0;
                termInContext = 0;
                if (index.get(doc).containsKey(word)) {
                    termFreq = index.get(doc).get(word);
                }
                if (totals.containsKey(word)) {
                    termInContext = totals.get(word);
                }
                // Jelinek-Mercer language model
                val = (0.5 * (Double.valueOf(termFreq) / Double.valueOf(docLenTotals.get(doc))));
                // Multiply each result for a given doc to get the total ranking for multi-term queries
                if (tally.containsKey(doc)) {
                    tally.put(doc, tally.get(doc) * val);
                } else {
                    tally.put(doc, val);
                }
            }
        }
        ArrayList<Map.Entry<Integer, Double>> sorter = new ArrayList<>();
        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();
        // Sorting the hashmap by value
        for (Map.Entry<Integer, Double> e: tally.entrySet()) {
            sorter.add(e);
        }
        // Creating a new comparator to determine how to compare values to each other
        Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> e1, Map.Entry<Integer, Double> e2) {
                Double d1 = e1.getValue();
                Double d2 = e2.getValue();
                return d2.compareTo(d1);
            }
        };
        // Adding values back to map in sorted order
        Collections.sort(sorter, valueComparator);
        for (Map.Entry<Integer, Double> e : sorter) {
            sortedMap.put(e.getKey(), e.getValue());
        }
        System.out.println(sortedMap);
        // Adding ranked docs and their scores to the ans array to be returned
        List<ResultClass> ans = new ArrayList<ResultClass>();
        for (Integer docID : sortedMap.keySet()) {
            Document doc = new Document("Doc" + docID);
            ResultClass rc = new ResultClass();
            rc.DocName = doc;
            rc.docScore = sortedMap.get(docID);
            ans.add(rc);
        }
        return ans;
    }
}
