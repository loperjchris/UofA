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
        self._word = line[0]
        self._pronunciation = [line[1:]]

    def add_pronunciation(self, line):
        self._pronunciation.append(line[1:])

    def get_word(self):
        return self._word

    def get_pronunciation(self):
        return self._pronunciation

    def __eq__(self, other):
        """
        Determines whether two words are perfect rhymes.

        Parameters: other is a Word object.

        Returns: True or False.

        Pre-condition: other is a completely initialized Word object with a
            pronunciation list.

        Post-condition: Must equal True or False.
        """
        rhyme1 = []
        rhyme2 = []
        # Finds the rhyming portion of the user given word.
        for i in range(len(self._pronunciation)):
            # Determines the rhyming portion of the word if the stressor
                # is in the first phoneme.
            if i == 0 and self._pronunciation[i][-1] == "1":
                rhyme1 = self._pronunciation[i:]
                # Inserts an empty element into the first position to allow
                    # for testing of if the phonemes before the stressor
                    # are the same.
                rhyme1.insert(0, "")
            elif self._pronunciation[i][-1] == "1":
                # Uses the phoneme before the stressor to test if they matche
                    # therefore indicating the words aren't perfect matches.
                rhyme1 = self._pronunciation[i-1:]
        # Determines the rhyming portion of each word in the dictionary in
            # the same way as for the user given word.
        for n in range(len(other._pronunciation)):
            if n == 0 and other._pronunciation[n][-1] == "1":
                rhyme2 = other._pronunciation[n:]
                # Inserts an empty element into the first position to allow
                    # for testing of if the phonemes before the stressor
                    # are the same.
                rhyme2.insert(0, "")
            elif other._pronunciation[n][-1] == "1":
                # Uses the phoneme before the stressor to test if they matche
                    # therefore indicating the words aren't perfect matches.
                rhyme2 = other._pronunciation[n-1:]
        # checks if the rhyming portions match and the phonemes before the
            # stressor does not.
        if rhyme1[1:] == rhyme2[1:] and rhyme1[0] != rhyme2[0]:
            return True
        return False

    def __str__(self):
        """
        Returns a string representation of a Word object.

        Parameters: None.

        Returns: A string with the word and its pronunciation.
        """
        return str(self._word) + ": " + str(self._pronunciation)

class WordMap:

    def __init__(self):
        """
        Initializes a WordMap object.

        Parameters: None.

        Returns: None.
        """
        self._word_dict = {}

    def add(self, line):
        """
        Adds a word and its pronunciation to the WordMap dictionary.

        Parameters: line is a list.

        Returns: None.

        Pre-condition: line must be a list with the word at postion 0 and
            the word's pronunciation following it.

        Post-condition: None.
        """
        # Removes any blank elements.
        if line[1] == "":
            del line[1]
        if line[0] not in self._word_dict:
            self._word_dict[line[0]] = Word(line)
        else:
            self._word_dict[line[0]].add_pronunciation(line)

    def in_word_dict(self, word):
        """
        Determines whether a word is already in the WordMap dictionary.

        Parameters: word is a string.

        Returns: True or False.

        Pre-condition: word must be a string.

        Post-condition: Must equal True or False.
        """
        return word in self._word_dict

    def check(self, word):
        """
        Gets each word in the WordMap dictionary and determines if that word
            and the user given word are perfect rhymes.

        Parameters: word is a string.

        Returns: word_list is a list.

        Pre-condition: word must be a string.

        Post-condition: word_list must be a list containing all the words
            that are perfect rhymes to the user given word.
        """
        word_list = []
        # Gets the two word objects to be compared.
        for word_object in self._word_dict[word]:
            for key in self._word_dict:
                for value in self._word_dict[key]:
                    # Adds the word to word_list if they rhyme, isn't the
                        # given word, and isn't already in word_list.
                    if word_object == value and key != word and\
                        key not in word_list:
                        word_list.append(key)
        return word_list

    def __str__(self):
        """
        Creates a string representation of the WordMap dictionary.

        Parameters: None.

        Returns: str is a string of the WordMap dictionary with the
            word as the key and the pronunciation as the values.
        """
        str = "{"
        for word, pronunciation in self._word_dict.items():
            # Adds the word.
            str += "'" + word + "': "
            # Uses a temp string to get each pronunciation of a word.
            temp = ""
            for item in pronunciation:
                temp += str(item.get_pronunciation()) + ", "
            temp = temp.rstrip(", ")
            # Adds the pronunciation.
            str += "[" + temp + "], "
        str = str.rstrip(", ")
        str += "}"
        return str

def main():
    word_dict = WordMap()
    create_dict(word_dict)
    return_rhymes(word_dict)

def create_dict(word_dict):
    """
    Reads in the pronuciation dictionary text file and adds each word to
        the WordMap dictionary.

    Parameters: word_dict is a WordMap object.

    Returns: None.

    Pre-condition: word_dict must be an initialized WordMap object.

    Post-condition: None.
    """
    wordlist = input()
    # Checks to see if the given file name can be found.
    try:
        wordlist = open(wordlist)
    except FileNotFoundError:
        # Exits the program if the file can't be found.
        print ("ERROR: Could not open file " + wordlist)
        exit(1)
    wordlist = wordlist.read().split("\n")
    # Feeds each word into the add method for WordMap objects.
    for line in wordlist:
        line = line.split(" ")
        if len(line) > 1:
            word_dict.add(line)

def return_rhymes(word_dict):
    """
    Gets a user generated word and finds the words that rhyme with it.

    Parameters: word_dict is a WordMap object.

    Returns: None.

    Pre-condition: word_dict must be an initialized WordMap object.

    Post-condition: None.
    """
    word = input()
    word = word.upper()
    # Checks if the given word is in the given pronunciation dictionary and
        # quits if it doesn't.
    try:
        assert word_dict.in_word_dict(word)
    except AssertionError:
        print ("ERROR: the word input by the user is not in the pronunciation dictionary " + word.lower())
        exit(1)
    rhymes = word_dict.check(word)
    # Prints each rhyming word, one per line.
    for element in rhymes:
        print (element.lower())

main()
