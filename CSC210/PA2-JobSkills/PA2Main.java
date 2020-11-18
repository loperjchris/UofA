
/*
 * AUTHOR: Ruben Tequida
 * FILE: PA2Main.java
 * ASSIGNMENT: Programming Assignment 2 - JobSkills
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program takes in an array of strings where the first element
 * is the filename, the second element is the command, and the third element
 * is the category only used when calling the LOCATIONS command. The file will
 * be a .csv file that contains information on job listings as given in the
 * example below. If the program is given the CATCOUNT command it will print
 * out each job category and the number of openings within that category. If
 * it's given the LOCATIONS command it will print out the name of the location
 * and the number of openings for the given category in that city. If neither
 * of those commands is given it will display "Invalid Command"
 * 
 * Example input file:
 * 
 * Company,Title,Category,Location,Responsibilities,Minimum Qualifications,Preferred Qualifications
 * Google,TitleA,CategoryX,Tel Aviv,Everything and the rest, BS, MS
 * Google,TitleB,CategoryX,Tel Aviv,Everything and the rest, BS, MS
 * Google,TitleB,CategoryY,Houston,Everything and the rest, BS, MS
 * Google,TitleC,CategoryX,Jonesboro,Everything and the rest, BS, MS 
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PA2Main {
    /*
     * Purpose: Calls on openFile, createMap, and process methods in order to
     * open the given file, put the necessary information into a map and then
     * process the given command and print out the correct information.
     * 
     * @param args, which takes in arguments from the command line in the
     * order filename, command, category (only for LOCATIONS command).
     * 
     * @return None.
     */
    public static void main(String[] args) {
        Scanner fileName = openFile(args);
        Map<String, Map<String, Integer>> dataMap = createMap(fileName);
        if (args[1].equals("CATCOUNT")) {
            processCategory(dataMap);
        } else if (args[1].equals("LOCATIONS")) {
            processLocation(dataMap, args);
        } else {
            System.out.println("Invalid Command");
        }
    }

    /*
     * Purpose: Attempts to open the file and returns the name if it can or
     * throws an exception if it can't.
     * 
     * @param args, which takes in arguments from the command line in the
     * order filename, command, category (only for LOCATIONS command).
     * 
     * @return fileName, which is the name of the given file.
     */
    public static Scanner openFile(String[] args) {
        // Attempts to open the file given.
        try {
            Scanner fileName = new Scanner(new File(args[0]));
            return fileName;
            // Prints an exception if it can't find the file.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /*
     * Purpose: Takes the information given and puts it into a map of maps where
     * the
     * first key is the category and its value is another map. The second map
     * key is the locations and its value is the number of job listings for
     * that category and location.
     * 
     * @param fileName, which is a scanner object and holds all the
     * information in the given file.
     * 
     * @return dataMap, which is a map of maps that holds the category,
     * location, and number of listings information.
     */
    public static Map<String, Map<String, Integer>> createMap(
            Scanner fileName) {
        // Skips the first line in the file.
        fileName.nextLine();
        Map<String, Map<String, Integer>> dataMap = new HashMap<String, Map<String, Integer>>();
        while (fileName.hasNextLine()) {
            // Creates an array out of the remaining information.
            String[] currentLine = fileName.nextLine().split(",");
            // Checks if the keys are already in the map and adds 1 if they are.
            if (dataMap.containsKey(currentLine[2])) {
                if (dataMap.get(currentLine[2]).containsKey(currentLine[3])) {
                    dataMap.get(currentLine[2]).put(currentLine[3],
                            dataMap.get(currentLine[2]).get(currentLine[3])
                                    + 1);
                } else {
                    // Adds the location to the map with a value of 1.
                    dataMap.get(currentLine[2]).put(currentLine[3], 1);
                }
            } else {
                // Adds the category and location to the map with a value of 1.
                Map<String, Integer> tempMap = new HashMap<String, Integer>();
                tempMap.put(currentLine[3], 1);
                dataMap.put(currentLine[2], tempMap);
            }
        }
        fileName.close();
        return dataMap;
    }

    /*
     * Purpose: Processes the CATCOUNT command by printing all categories and
     * the number of job listings in those categories.
     * 
     * @param dataMap, which is a map of maps that holds the category,
     * location, and number of listings information.
     * 
     * @return None.
     */
    public static void processCategory(
            Map<String, Map<String, Integer>> dataMap) {
        // Sorts the category keys.
        ArrayList<String> sortedKeys = new ArrayList<String>(dataMap.keySet());
        Collections.sort(sortedKeys);
        System.out.println("Number of positions for each category\n"
                + "-------------------------------------");
        for (String category : sortedKeys) {
            int totalListings = 0;
            // Gets a running total for each category.
            for (int num : dataMap.get(category).values()) {
                totalListings += num;
            }
            System.out.println(category + ", " + String.valueOf(totalListings));
        }
    }

    /*
     * Purpose: Processes the LOCATIONS command by printing all locations for
     * a given category and the number of job listings in that categories.
     * 
     * @param dataMap, which is a map of maps that holds the category,
     * location, and number of listings information. args, which takes in
     * arguments from the command line in the order filename, command,
     * category (only for LOCATIONS command).
     * 
     * @return None.
     */
    public static void processLocation(
            Map<String, Map<String, Integer>> dataMap, String[] args) {
        System.out.println("LOCATIONS for category: " + args[2] + "\n"
                + "-------------------------------------");
        if (dataMap.containsKey(args[2])) {
            // Sorts the location keys.
            ArrayList<String> sortedKeys = new ArrayList<String>(
                    dataMap.get(args[2]).keySet());
            Collections.sort(sortedKeys);
            for (String location : sortedKeys) {
                // Prints the location and the number of listings for the
                // category and location.
                System.out.println(
                        location + ", "
                                + dataMap.get(args[2]).get(location));
            }
        }
    }
}

