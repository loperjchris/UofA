"""
    File: rhymes-oo.py
    Author: Ruben Tequida
    Purpose: Returns a list of perfect rhymes for a given word.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from sys import *

class Word:

    def __init__(self, line):
        """
        Initializes a Word object and all of its attributes.

        Parameters: line is a list.

        Returns: None.

        Pre-condition: line is a list with the word at element 0 and each
            phoneme being the following elements.

        Post-condition: None.
        """
        self._word = line[0].lower()
        pronunciation = self.rhyme_portion(line)
        self._pronunciation = [pronunciation]

    def rhyme_portion(self, line):
        """
        Identifies the rhyming portion of the phoneme list.

        Parameters: line is a list.

        Returns: pronuncation is a list.

        Pre-condition: line is a list with the word at element 0 and each
            phoneme being the following elements.

        Post-condition: pronunciation is a list containing the rhyming portion
            of the pronunciation.
        """
        n = 0
        # Removes any blank elements that may be in the line list.
        while n < len(line):
            if line[n] == "":
                del line[n]
            else:
                n += 1
        for i in range(1, len(line)):
            # Gets the phoneme before the stressors to test if they are the
                # same which would indicate a non-perfect rhyme.
            if line[i][-1] == "1" and i != 1:
                pronunciation = line[i - 1:]
                break
            # If the stressor is the first element or the word doesn't have
                # a stressor this will grab the entire pronunciation and add
                # a blank element at the front.
            else:
                pronunciation = line[1:]
                pronunciation.insert(0, "")
        return pronunciation

    def add_pronunciation(self, line):
        """
        Appends the pronunciation of a Word object if it has more than one
            pronunciation.

        Parameters: line is a list.

        Returns: None.

        Pre-condition: line is a list with the word at element 0 and each
            phoneme being the following elements.

        Post-condition: None.
        """
        pronunciation = self.rhyme_portion(line)
        self._pronunciation.append(pronunciation)

    def get_pronunciation(self):
        """
        Getter method for the list of pronunciations of a word.
        """
        return self._pronunciation

    def get_word(self):
        """
        Getter method for the word of a Word object.
        """
        return self._word

    def __eq__(self, other):
        """
        Determines whether two Word objects have a perfect rhyme among any of
            their pronunciations.

        Parameters: other is a Word object.

        Returns: True or False.

        Pre-condition: other is a Word object.

        Post-condition: Must evaluate to True or False.
        """
        for pronun1 in self._pronunciation:
            for pronun2 in other._pronunciation:
                if pronun1[1:] == pronun2[1:] and pronun1[0] != pronun2[0]:
                    return True
        return False

    def __str__(self):
        """
        Creates a string that's printed when print is called on a Word
            object.

        Parameters: None

        Returns: A string with the word and a list of its pronunciations
            separated by a colon.
        """
        return "'" + self._word + "': " + str(self._pronunciation)

class WordMap:

    def __init__(self):
        """
        Initializes a WordMap object dictionary.

        Parameters: None.

        Returns: None.
        """
        self._word_dict = {}

    def create_dict(self):
        """
        Opens the dictionary file and inputs every word and its pronunciation
            into the WordMap dictionary.

        Parameters: None.

        Returns: None.
        """
        dictionary = input()
        # Tests whether the file can be opened and quits if it can't.
        try:
            dictionary = open(dictionary)
        except FileNotFoundError:
            print ("ERROR: Could not open file " + dictionary)
            exit(1)
        dictionary = dictionary.read().split("\n")
        # Reads in every line of the given file and adds the words to the
            # WordMap dictionary as Word objects or appended to the Word
            # object pronunciation list.
        for line in dictionary:
            line = line.split(" ")
            # Checks if the line has a word and pronunciation
            if len(line) > 1:
                line[0] = line[0].lower()
                if line[0] not in self._word_dict:
                    self._word_dict[line[0]] = Word(line)
                else:
                    self._word_dict[line[0]].add_pronunciation(line)

    def rhyme(self):
        """
        Gets the user given word and prints all the words that are perfect
            rhymes with it.

        Parameters: None.

        Returns: None.
        """
        word = input()
        word = word.lower()
        rhyme_list = []
        # Tests whether the word is a part of the supplied dictionary file
            # and quits if it isn't.
        try:
            assert word in self._word_dict
        except AssertionError:
            print ("ERROR: the word input by the user is not in the\
                pronunciation dictionary " + word)
            exit(1)
        # Determines which words rhyme and if they are already included in the
            # rhyme list
        for key in self._word_dict:
            if self._word_dict[word] == self._word_dict[key] and key not in\
                rhyme_list:
                rhyme_list.append(key)
        # Prints out every item of rhyme_list one by one.
        for item in rhyme_list:
            print (item)

    def get_dictionary(self):
        """
        Getter method for the WordMap dictionary.
        """
        return self._word_dict

    def __str__(self):
        """
        Creates a string representation of a WordMap object dictionary.

        Parameters: None.

        Returns: A string with the keys and associated values in the WordMap
            dictionary.
        """
        dict_rep = "{"
        for value in self._word_dict.values():
            dict_rep += str(value) + ", "
        dict_rep = dict_rep.rstrip(", ")
        dict_rep += "}"
        return dict_rep

def main():
    """
    Creates the WordMap dictionary and starts the rhyme function.

    Parameters: None.

    Returns: None.
    """
    word_dict = WordMap()
    word_dict.create_dict()
    word_dict.rhyme()

main()
