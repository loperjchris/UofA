
/*
 * AUTHOR: Ruben Tequida
 * FILE: PA1Main.java
 * ASSIGNMENT: Programming Assignment 1 - Gradenator
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program gets a name of a text file and reads in its contents.
 * The contents of the text file will contain a list of grades, the category
 * for which those grades apply, and the percentage for which that category
 * counts towards the overall grade. The program will print out the average
 * of each category, the name of the category, and its percentage towards the
 * overall grade. Then, it'll print the percentage received and the total 
 * percentage it was out of.
 * 
 * Example input file:
 * 
 * 80; final; 20%
 * 90; programming assignments; 30%
 * 70; midterm 2; 10%
 * 90; sections; 10%
 * 90 60 50; quizzes; 10%
 * 60; midterm 1; 10%
 * 80; peer reviews and drills; 10%
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PA1Main {
    /*
     * Purpose: Calls on getFileName method to get the filename from the user
     * and the calculateGrades method which determines the output for the
     * program.
     * 
     * @param None.
     * 
     * @return None.
     */
    public static void main(String[] args) {
        String fileName;
        fileName = getFileName();
        calculateGrades(fileName);
    }

    /*
     * Purpose: Prompts the user to give a filename and returns it.
     * 
     * @param None.
     * 
     * @return fileName, which is a string that contains the name of the file.
     */
    public static String getFileName() {
        Scanner fileNameInput = new Scanner(System.in);
        System.out.println("File name?");
        String fileName = fileNameInput.next();
        fileNameInput.close();
        return fileName;
    }

    /*
     * Purpose: Calculates the grades, averages, and totals for each category
     * being graded and prints them out for each category. Also calculates
     * the total percentage earned for the grades received and that percentage
     * out of the total possible percentage that could have been received.
     * 
     * @param fileName, which is a string that contains the name of the file.
     * 
     * @return None.
     */
    public static void calculateGrades(String fileName) {
        Double averageTemp = 0.0;
        Double averageTotal = 0.0;
        Double total = 0.0;
        Double totalTemp = 0.0;
        // Attempts to open the filename given.
        try {
            Scanner contents = new Scanner(new File(fileName));
            while (contents.hasNextLine()) {
                // Formatting the data to be properly manipulated.
                String[] line = contents.nextLine().split(";");
                String[] grades = line[0].trim().split(" ");
                Double gradeTotal = 0.0;
                // Finding the average grade for a given category.
                for (String grade : grades) {
                    gradeTotal += Double.parseDouble(grade);
                }
                averageTemp = (double) gradeTotal / grades.length;
                totalTemp = Double.parseDouble(
                        line[2].substring(0, line[2].length() - 1));
                // Keeping a running total to display total received out of
                // total possible.
                total += totalTemp;
                averageTotal += averageTemp * totalTemp * 0.01;
                System.out.printf("%s; %.1f%%; avg=%.1f\n", line[1].trim(),
                        totalTemp, averageTemp);
            }
            contents.close();
            // Throws an exception if the filename given cannot be found.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.printf("TOTAL = %.1f%% out of %.1f%%\n", averageTotal,
                total);
    }
}