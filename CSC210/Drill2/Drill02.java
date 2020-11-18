package edu.spr19cs210.drill02;

public class Drill02 {

    // takes a number of characters to print as a parameter
    // returns a string with one or two asterisks in the middle
    // surrounded by alligators
    public static String zigzag(int n) {
        if (n == 1) {
            return "*";
        }
        if (n == 2) {
            return "**";
        }
        return "<" + zigzag(n - 2) + ">";
    }

    // takes a string and a character as parameters and returns 
    // the string with all copies of the character moved to the 
    // end and capitalized
    public static String moveToEnd(String s, char c) {
        String tempS = s.toLowerCase();
        if (!s.contains(Character.toString(c))) {
            return s;
        }
        int charIndex = tempS.indexOf(Character.toLowerCase(c));
        return s.substring(0, charIndex)
                + moveToEnd(s.substring(charIndex + 1), c)
                + Character.toUpperCase(c);
    }

    // takes a string and two characters as parameters
    // returns a string that is the same as the passed in string
    // except that all occurrences of the first character are replaced
    // with the second
    public static String replaceAll(String s, char from, char to) {
        if (s.equals("")) {
            return "";
        }
        if (s.substring(0, 1).equals(Character.toString(from))) {
            return to + replaceAll(s.substring(1), from, to);
        }
        return s.substring(0, 1) + replaceAll(s.substring(1), from, to);
    }

    // takes an integer as a parameter
    // returns true if the digits are in sorted order ascending, 
    // false otherwise
    public static boolean digitsSorted(int x) {
        if (x < 0) {
            x *= -1;
        }
        int xLength = String.valueOf(x).length();
        if (xLength == 1) {
            return true;
        }
        if (x % 10 < (x % 100) / 10) {
            return false;
        }
        return digitsSorted(x / 10);
    }

}
