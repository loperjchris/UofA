
/* AUTHOR: Ruben Tequida
 * FILE: PA7Main.java
 * ASSIGNMENT: Programming Assignment 7 - HashTablempl
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program utilizes a hashmap that is created using generics.
 * Therefore, any type parameter can be passed as the key as well as the value
 * of the hashmap entries. After creating a new MyHashMap object the program
 * will proceed to open a csv file that contains all the information to be
 * gathered by the program. The program takes in an array of arguments where
 * the first argument is the name of the file, the second is the command that
 * determines what the program is looking for and a third optional command
 * associated with LOCATIONS as a second position command. There are 3 possible
 * commands that can go in the second position: CATCOUNT, LOCATIONS, and DEBUG.
 * CATCOUNT will produce the number of each category that appears in the given
 * csv. LOCATIONS will give each each location and the number of times they
 * appear in the file for a given category. DEBUG will fill the hashmap with
 * every category and then print out a visualization of the hashmap buckets and
 * shows how many collisions occured while filling the hashmap.
 * 
 * example input file:
 * 
 * Company,Title,Category,Location,Responsibilities,Minimum Qualifications,
 *      Preferred Qualifications
 * Google,TitleA,CategoryX,Tel Aviv,Everything and the rest, BS, MS
 * Google,TitleB,CategoryX,Tel Aviv,Everything and the rest, BS, MS
 * Google,TitleB,CategoryY,Houston,Everything and the rest, BS, MS
 * Google,TitleC,CategoryX,Jonesboro,Everything and the rest, BS, MS 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class PA7Main {
    /*
     * Purpose: Creates a MyHashMap object, opens the given file, and evaluates
     * the given command in the second position of the argument.
     * 
     * @param a, which takes in arguments from the command line in the order
     * filename, command (CATCOUNT, LOCATIONS, or DEBUG), and category for the
     * LOCATIONS command.
     * 
     * @return None.
     */
    public static void main(String[] a) {
        MyHashMap<String, Integer> map = new MyHashMap<String, Integer>();
        List<String> entries = openFile(a[0]);
        if (a[1].equals("CATCOUNT")) {
            System.out.println("Number of positions for each category\n"
                    + "-------------------------------------");
            createMap(map, entries, null, 2, false);
        } else if (a[1].equals("LOCATIONS")) {
            System.out.println("LOCATIONS for category: " + a[2] + "\n"
                    + "-------------------------------------");
            createMap(map, entries, a[2], 3, false);
        } else if (a[1].equals("DEBUG")) {
            System.out.println("DEBUG output for MyTable\n"
                    + "-------------------------------------");
            createMap(map, entries, null, 2, true);
        } else {
            System.out.println("Invalid Command");
        }
    }

    /*
     * Purpose: Opens the given filename and fills a list with all the table
     * entries in the file.
     * 
     * @param fileName, which is the name of the file that needs to be opened.
     * 
     * @return entries, which is a list of strings containing all entries given
     * in the csv file.
     */
    public static List<String> openFile(String fileName) {
        Scanner file = null;
        List<String> entries = new ArrayList<String>();
        // Attempts to open the given filename.
        try {
            file = new Scanner(new File(fileName));
            // Throws an exception and ends the program if the file isn't found.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        file.nextLine();
        while (file.hasNextLine()) {
            entries.add(file.nextLine());
        }
        return entries;
    }

    /*
     * Purpose: Fills MyHashMap object with the proper key determined by the
     * given command and increases its value by 1 for every time it appears.
     * 
     * @param map, is a MyHashMap object that holds the categories or locations
     * that appear in the csv. entries, is a list of all the entries that were
     * in the csv. cat, is the category that needs to be looked for if
     * LOCATIONS is given as the command. entrylocation, is the index of
     * entries that will be pulled and used as the map key. debug, is a boolean
     * that tells if the DEBUG command was used or not.
     * 
     * @return None.
     */
    public static void createMap(MyHashMap<String, Integer> map,
            List<String> entries, String cat, int entryLocation,
            boolean debug) {
        for (String entry : entries) {
            String[] temp = entry.split(",");
            // Skips invalid entries and only evaluates necessary categories.
            if (temp.length == 7 && (temp[2].equals(cat) || cat == null)) {
                // Increasing the value if the key is already in the map.
                if (map.containsKey(temp[entryLocation])) {
                    map.put(temp[entryLocation],
                            map.get(temp[entryLocation]) + 1);
                    // Creating a new map entry.
                } else {
                    map.put(temp[entryLocation], 1);
                }
            }
        }
        if (debug == false) {
            printResults(map);
        } else {
            map.printTable();
        }
    }

    /*
     * Purpose: Prints the contents of the hashmap as key and value pairs.
     * 
     * @param map, which is a MyHashMap object and holds all the desired key
     * and value pairs.
     * 
     * @return None.
     */
    public static void printResults(MyHashMap<String, Integer> map) {
        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println(key + ", " + map.get(key));
        }
    }
}
