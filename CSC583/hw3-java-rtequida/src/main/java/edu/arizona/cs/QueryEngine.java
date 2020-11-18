/*
 * AUTHOR: Ruben Tequida
 * FILE: QueryEngine.java
 * ASSIGNMENT: Assignment 3
 * COURSE: CSC 583; Fall 2020
 * Purpose: This program utilizes Lucene in order to create create
 * and inverted index and uses Lucene queries to get the documents
 * that match the query as well as the documents similarity score
 * to that query.
 */

package edu.arizona.cs;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.*;

import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class QueryEngine {
    boolean indexExists=false;
    String inputFilePath ="";
    Directory index;
    StandardAnalyzer analyzer;
    IndexReader reader;
    IndexSearcher searcher;
    Map<Float, String> results;

    public QueryEngine(String inputFile){
        inputFilePath =inputFile;
        buildIndex();
    }

    /*
     * Purpose: Creates an inverted index from the input file passed to it
     * using Lucene.
     */
    private void buildIndex() {
        // Defining path for directory to hold index
        String dirPath = "src/main/resources/index";
        Path path = FileSystems.getDefault().getPath(dirPath);
        // Checking if directory exists or not and creating it if it doesn't
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        // Attempting to open the index
        try {
            index = FSDirectory.open(path);
            // Check if index is already created
            if (!DirectoryReader.indexExists(index)) {
                // Attempting to open input text file
                try (Scanner inputScanner = new Scanner(new File("src/main/resources/" + inputFilePath))) {
                    analyzer = new StandardAnalyzer();
                    IndexWriterConfig config = new IndexWriterConfig(analyzer);
                    config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                    IndexWriter w = new IndexWriter(index, config);
                    // Adding each line in input to the index
                    while (inputScanner.hasNextLine()) {
                        String line = inputScanner.nextLine();
                        Document doc = new Document();
                        doc.add(new TextField("title", line.substring(5), Field.Store.YES));
                        doc.add(new StringField("docid", line.substring(0, 4), Field.Store.YES));
                        w.addDocument(doc);
                    }
                    w.close();
                    inputScanner.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                analyzer = new StandardAnalyzer();
            }
            indexExists = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Purpose: Initializes the input file, creates the index, asks the user which
     * method they wish to run and what their query is. Calls the appropriate
     * method and prints out the results
     */
    public static void main(String[] args ) {
        try {
            String fileName = "input.txt";
            System.out.println("********Welcome to  Homework 3!");
            // Create index
            QueryEngine objQueryEngine = new QueryEngine(fileName);
            Scanner input = new Scanner(System.in);
            System.out.println("Which method would you like to run: ");
            String method = input.nextLine();
            List<ResultClass> ans = new ArrayList<>();
            String[] query13a = {"information", "retrieval"};
            if (method.equals("1")) {
                ans = objQueryEngine.runQ1_1(query13a);
            } else if (method.equals("2")) {
                ans = objQueryEngine.runQ1_2_a(query13a);
            } else if (method.equals("3")) {
                ans = objQueryEngine.runQ1_2_b(query13a);
            } else if (method.equals("4")) {
                ans = objQueryEngine.runQ1_2_c(query13a);
            } else {
                ans = objQueryEngine.runQ1_3(query13a);
            }
            input.close();
            for (ResultClass doc : ans) {
                System.out.println(doc.DocName.get("docid") + " " + doc.DocName.get("title"));
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
     * Purpose: Queries the index with a normal string.
     *
     * @param: query, an array that is the words to search.
     *
     * @return: ans, a list of ResultClass objects that match the query
     */
    public List<ResultClass> runQ1_1(String[] query) throws java.io.FileNotFoundException, java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        // Initialize reader and searcher objects
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        String que = "";
        List<ResultClass> ans = new ArrayList<ResultClass>();
        // Put query in proper format
        for (String str : query) {
            que += str + " ";
        }
        que = que.substring(0, que.length() - 1);
        ans = getDocs(que);
        // Print out results by score from highest to lowest
        System.out.println("Found " + results.keySet().size() + " hit(s).");
        for (Float score : results.keySet()) {
            System.out.println("ID: " + results.get(score) + "\tscore: " + score);
        }
        return ans;
    }

    /*
     * Purpose: Queries the index with a boolean AND string.
     *
     * @param: query, an array that is the words to search.
     *
     * @return: ans, a list of ResultClass objects that match the query
     */
    public List<ResultClass> runQ1_2_a(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        // Initialize reader and searcher objects
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        List<ResultClass> ans = new ArrayList<ResultClass>();
        String que = "";
        // Put query in proper format
        for (String str : query) {
            que += str + " AND ";
        }
        que = que.substring(0, que.length() - 5);
        ans = getDocs(que);
        // Print out results by score from highest to lowest
        System.out.println("Found " + results.keySet().size() + " hit(s).");
        for (Float score : results.keySet()) {
            System.out.println("ID: " + results.get(score) + "\tscore: " + score);
        }
        return ans;
    }

    /*
     * Purpose: Queries the index with a boolean AND NOT string.
     *
     * @param: query, an array that is the words to search.
     *
     * @return: ans, a list of ResultClass objects that match the query
     */
    public List<ResultClass> runQ1_2_b(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        // Initialize reader and searcher objects
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        List<ResultClass> ans = new ArrayList<ResultClass>();
        String que = "";
        // Put query in proper format
        for (String str : query) {
            que += str + " AND NOT ";
        }
        que = que.substring(0, que.length() - 9);
        ans = getDocs(que);
        // Print out results by score from highest to lowest
        System.out.println("Found " + results.keySet().size() + " hit(s).");
        for (Float score : results.keySet()) {
            System.out.println("ID: " + results.get(score) + "\tscore: " + score);
        }
        return ans;
    }

    /*
     * Purpose: Queries the index with a proximity query.
     *
     * @param: query, an array that is the words to search.
     *
     * @return: ans, a list of ResultClass objects that match the query
     */
    public List<ResultClass> runQ1_2_c(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        // Initialize reader and searcher objects
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        List<ResultClass> ans = new ArrayList<ResultClass>();
        String que = "\"";
        // Put query in proper format
        for (String str : query) {
            que += str + " ";
        }
        que = que.substring(0, que.length() - 1);
        que += "\"~1";
        ans = getDocs(que);
        // Print out results by score from highest to lowest
        System.out.println("Found " + results.keySet().size() + " hit(s).");
        for (Float score : results.keySet()) {
            System.out.println("ID: " + results.get(score) + "\tscore: " + score);
        }
        return ans;
    }

    /*
     * Purpose: Queries the index with a string query and changes the type of
     * scoring method used.
     *
     * @param: query, an array that is the words to search.
     *
     * @return: ans, a list of ResultClass objects that match the query
     */
    public List<ResultClass> runQ1_3(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        // Initialize reader and searcher objects
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        String que = "";
        // Put query in proper format
        List<ResultClass> ans = new ArrayList<ResultClass>();
        for (String str : query) {
            que += str + " ";
        }
        Similarity sim = new BM25Similarity();
        que = que.substring(0, que.length() - 1);
        ans = getDocs(que);
        // Print out results by score from highest to lowest using first method of scoring
        System.out.println("Found " + results.keySet().size() + " hit(s).");
        System.out.println("For similarity BM25 the scores were: ");
        for (Float score : results.keySet()) {
            System.out.println("ID: " + results.get(score) + "\tscore: " + score);
        }
        // Change scoring method
        searcher.setSimilarity(new ClassicSimilarity());
        getDocs(que);
        // Print out results by score from highest to lowest using second method of scoring
        System.out.println("For similarity TF-IDF the scores were: ");
        for (Float score : results.keySet()) {
            System.out.println("ID: " + results.get(score) + "\tscore: " + score);
        }
        return ans;
    }

    /*
     * Purpose: Queries the index with the passed in query and fills a dictionary
     * and list with the documents that match the query as well as their scores.
     *
     * @param: query, a string that is the query.
     *
     * @return: ans, a list of ResultClass objects that match the query
     */
    private List<ResultClass> getDocs(String query) throws FileNotFoundException, IOException {
        List<ResultClass> result = new ArrayList<ResultClass>();
        // Create a treemap to hold the documents and their scores
        results = new TreeMap<>(Collections.reverseOrder());
        try {
            Query q = new QueryParser("title", analyzer).parse(query);
            int hitsPerPage = 10;
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            // Go through all the hits to get the documents that matched and their scores
            for(int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                ScoreDoc sd = hits[i];
                Document d = searcher.doc(docId);
                // Add the document and its score to the results dictionary
                results.put(sd.score, d.get("docid"));
                ResultClass obj = new ResultClass();
                obj.DocName = d;
                // Add the document to the results list
                result.add(obj);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
