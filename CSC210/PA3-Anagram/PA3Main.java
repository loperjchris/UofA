
/*
 * AUTHOR: Ruben Tequida
 * FILE: PA3Main.java
 * ASSIGNMENT: Programming Assignment 3 - Anagrams
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program takes in an array of strings where the first element
 * is the filename, the second is the word to be made into anagrams, and the
 * third element is the word limit for the anagram list (0 is no word limit).
 * The program opens and reads in the dictionary given and compiles a list of
 * all the words that can be spelled from the letters in the original word
 * given. The program then compiles a list of possible anagram combinations
 * that utilize all the letters in the original word given and keep to the
 * size limit if there is one.
 * 
 * An example of a dictionary would be a text file that has one word per line.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PA3Main {
    /*
     * Purpose: Creates the initial LetterInventory object of the words given.
     * Calls on the functions findContainedWords and printAnagrams and prints
     * out the beginning segments of the desired output.
     * 
     * @param args, which takes in arguments from the command line in the
     * order filename, word, limit.
     * 
     * @return None.
     */
    public static void main(String[] args) {
        LetterInventory letters = new LetterInventory(args[1]);
        List<String> containedWords = findContainedWords(args, letters);
        System.out.println("Phrase to scramble: " + args[1]
                + "\n\nAll words found in " + args[1] + ":\n" + containedWords
                + "\n\nAnagrams for " + args[1] + ":");
        printAnagrams(args[1], Integer.valueOf(args[2]), letters,
                containedWords,
                new ArrayList<String>());
    }

    /*
     * Purpose: Goes through each word in the given dictionary file and adds
     * that word to a list if that word can be made of letters from the
     * original word given.
     * 
     * @param args, which takes in arguments from the command line in the
     * order filename, word, limit. letters, which is a LetterInventory object
     * of the original word given.
     * 
     * @return anagrams, which is a list of all the words in the dictionary
     * that are contained in letters.
     */
    public static List<String> findContainedWords(String[] args,
            LetterInventory letters) {
        List<String> anagrams = new ArrayList<String>();
        Scanner fileName = null;
        // Attempts to open the given filename.
        try {
            fileName = new Scanner(new File(args[0]));
            // Throws an exception if the given filename can't be found.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        while (fileName.hasNextLine()) {
            String currentWord = fileName.nextLine();
            if (letters.contains(currentWord)) {
                anagrams.add(currentWord);
            }
        }
        fileName.close();
        return anagrams;
    }

    /*
     * Purpose: Creates and prints a list of words that will form anagrams out
     * of the original word given and won't contain more words than the limit
     * given.
     * 
     * @param originalWord, which is the original word given. limit, which is
     * the word limit given. letters, which is the LetterInventory object of
     * the original word given. containedWords, which is a list of words whose
     * letters are all contained in letters. sofar, which is a list of words
     * checked so far.
     * 
     * @return None.
     */
    public static void printAnagrams(String originalWord, int limit,
            LetterInventory letters, List<String> containedWords,
            List<String> sofar) {
        // Base case: all letters in letters have been used.
        if (letters.isEmpty()) {
            System.out.println(sofar);
        }
        for (String word : containedWords) {
            // Checks if letters can still form word and if the limit has been
            // reached.
            if (letters.contains(word)
                    && (sofar.size() < limit || limit == 0)) {
                // Choosing.
                sofar.add(word);
                letters.subtract(word);
                // Recursing.
                printAnagrams(originalWord, limit, letters, containedWords,
                        sofar);
                // Unchoosing.
                letters.add(sofar.remove(sofar.size() - 1));
            }
        }
    }
}
