"""
    File: fake-news-ms.py
    Author: Ruben Tequida
    Purpose: Prints words from a csv file that appear in the file a certain
        number of times.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from sys import *
import csv
import string
import sys

# Increases the maximum recursion depth to 2500.
sys.setrecursionlimit(2500)

class Word:

    def __init__(self, word):
        """
        Initializes a Word class object and all of its attributes.

        Parameters: word is a string.

        Returns: None.

        Pre-condition: word is a string gathered from the given csv file.

        Post-condition: None.
        """
        self._word = word
        self._count = 1

    def word(self):
        """
        Getter method for the word attribute.
        """
        return self._word

    def count(self):
        """
        Getter method for the count attribute.
        """
        return self._count

    def incr(self):
        """
        Increases the count of a word.

        Parameters: None.

        Returns: None.
        """
        self._count += 1

    def __str__(self):
        """
        Prints a Word class object.

        Parameters: None.

        Returns: A string representation of a word and its count.
        """
        return self._word + " " + str(self._count)

def main():
    """
    Opens and reads in the file and calls functions to format the data, create
        the word objects and add them to the list, and print the words that
        match the criteria.
    """
    file = open_file()
    word_list = format_file(file)
    new_list = add_to_list(word_list)
    sorted_list = msort(new_list)
    print_words(sorted_list)

def open_file():
    """
    Checks if the given file name can be found and opened then reads the csv
        format.

    Parameters: None.

    Returns: A list of all items within the given file.
    """
    filename = input("File: ")
    # Trys to open the given file and exits the program if it can't.
    try:
        filename = open(filename)
    except FileNotFoundError:
        print ("ERROR: Could not open file " + filename)
        exit(1)
    return csv.reader(filename)

def format_file(file):
    """
    Formats the data so it can be manipulated properly.

    Parameters: file is a list

    Returns: word_list is a list.

    Pre-condition: file is a list created from the csv file.

    Post-condition: word_list is a properly formatted list of all the words
        in a specific field of the csv file.
    """
    word_list = []
    word_string = ""
    for item in file:
        # Ignores the comment lines
        if item[0][0] != "#":
            word_string += " "
            # Adds the none punctuation characters to the string and changes
                # punctuation characters into blank spaces.
            for char in item[4]:
                if char not in string.punctuation:
                    word_string += char
                else:
                    word_string += " "
    word_list = word_string.lower().split(" ")
    # Removes any elements that are less than 3 characters long.
    for element in word_list[:]:
        if len(element) < 3:
            word_list.remove(element)
    return word_list

def add_to_list(word_list):
    """
    Adds each word to the list and keeps track of how many times it appeared.

    Parameters: word_list is a list

    Returns: new_list is a list.

    Pre-condition: word_list is a properly formatted list of all the words
        in a specific field of the csv file.

    Post-condition: new_list is a list of Word objects.
    """
    new_list = []
    for word in word_list:
        found = False
        # Runs through new_list to see if the word has already been added and
            # increments the count if it has.
        for object in new_list:
            if word == object.word():
                object.incr()
                found = True
        # Creates a new Word object if it's not already in new_list.
        if not found:
            new_list.append(Word(word))
    return new_list

def msort(L):
    """
    Begins the process of sorting the list by splitting it into smaller lists
        then merging them. Derived from lecture slides.

    Parameters: L is a list.

    Returns: a list.

    Pre-condition: L must be a full list of words.

    Post-condition: L will be a completely sorted list of words.
    """
    # Base case of 1 element in the list.
    if len(L) <= 1:
        return L
    else:
        # Splits the list in two and sends it through the function again.
        split_pt = len(L) // 2
        L1 = L[:split_pt]
        L2 = L[split_pt:]
        sortedL1 = msort(L1)
        sortedL2 = msort(L2)
        # Merges the lists once they become lists of length 1.
        return merge(sortedL1, sortedL2)

def merge(L1, L2):
    """
    Merges two lists in order by count and then alphabetically. Derived from
        lecture slides then modified.

    Parameters: L1 and L2 are lists.

    Returns: a list.

    Pre-condition: L1 and L2 are one object lists or previously merged lists
        that will be merged with each other.

    Post-condition: Will return a merged and sorted list.
    """
    # Base case of one of the lists being empty.
    if L1 == [] or L2 == []:
        return L1 + L2
    else:
        # Adds the first element of the first list if it has a higher count.
        if L1[0].count() > L2[0].count():
            return [L1[0]] + merge(L1[1:], L2)
        # Adds the first element of the second list if it has a higher count.
        elif L1[0].count() < L2[0].count():
            return [L2[0]] + merge(L1, L2[1:])
        else:
            # Resorts to alphabetical sorting if the counts match.
            if L1[0].word() < L2[0].word():
                return [L1[0]] + merge(L1[1:], L2)
            else:
                return [L2[0]] + merge(L1, L2[1:])

def print_words(L):
    """
    Finds the Word object and position N and prints every word with a count
        greater than or equal to that Word object's count.

    Parameters: L is a list.

    Returns: None.

    Pre-condition: L is a completely sorted list by count and then
        alphabetically.

    Post-condition: None.
    """
    # Checks to see if the given input can be cast as an integer.
    try:
        N = int(input("N: "))
    except ValueError:
        print ("ERROR: Could not read N")
        exit(1)
    # Ends the program if the given number is negative.
    assert N >= 0
    N_word_count = L[N].count()
    # Prints every word that has a count greater than or equal to the count of
        # the object at position N.
    for item in L:
        if item.count() >= N_word_count:
            print ("{} : {:d}".format(item.word(), item.count()))
        else:
            break

main()
