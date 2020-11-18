"""
    File: ngrams.py
    Author: Ruben Tequida
    Purpose: Find and return the ngrams with the highest occurence within a
        user provided text document.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from sys import *

class Input:

    def __init__(self):
        """
        Initializes an Input object.

        Parameters: None.

        Returns: None.
        """
        filename = input()
        # Catching whether the file could be opened or not.
        try:
            self._file = open(filename)
        except:
            print ("ERROR: Could not open file " + filename)
            exit(1)

    def wordlist(self):
        """
        Creates a list of words from the original input file that have been
            formatted properly.

        Parameters: None.

        Returns: word_list is a list of words formatted for manipulation.

        Pre-condition: None.

        Post-condition: word_list must be a list of strings.
        """
        self._data = self._file.read().replace("\n", " ")\
            .replace('"', " ").split(" ")
        self._file.close()
        word_list = []
        # Removes punctuation and other symbols from the given words and
            # appends them to word_list.
        for element in self._data:
            assert isinstance(element, str)
            element = element.strip("!,:';.-$")
            if element != "":
                word_list.append(element.lower())
        return word_list

class Ngrams:

    def __init__(self):
        """
        Initializes an Ngrams object and an empty dictionary.

        Parameters: None.

        Returns: None.
        """
        self._n = int(input())
        self._ngram_dict = {}

    def update(self, ngram):
        """
        Updates the ngram_dict with the passed through ngram.

        Parameters: ngram is a string.

        Returns: None.

        Pre-condition: ngram must be a string.

        Post-condition: None.
        """
        ngram = " ".join(ngram)
        # Adds the ngram to the dictionary if it's not already a key and
            # increases the count of its occurences by 1.
        if ngram not in self._ngram_dict:
            self._ngram_dict[ngram] = 0
        self._ngram_dict[ngram] += 1

    def process_wordlist(self, wordlist):
        """
        Finds every ngram of length n within the wordlist and passes the ngram
            to the update method.

        Parameters: wordlist is a list of strings.

        Returns: None.

        Pre-condition: wordlist must be a list of strings that is properly
            formatted.

        Post-condition: None.
        """
        # Pulls every ngram of length n from the wordlist and passes it to
            #the update method.
        for place in range(len(wordlist) - self._n + 1):
            ngram = wordlist[place : place + self._n]
            self.update(ngram)

    def print_max_ngrams(self):
        """
        Prints the ngrams with the most occurences in the given text
            document.

        Parameters: None.

        Returns: None.
        """
        max_value = 0
        max_words = []
        # Searches the dictionary for the greatest number of occurences among
            # all ngrams.
        for key in self._ngram_dict:
            assert isinstance(self._ngram_dict[key], int)
            max_value = max(max_value, self._ngram_dict[key])
        # Prints all ngrams with the greatest number of occurences.
        for key in self._ngram_dict:
            assert isinstance(self._ngram_dict[key], int)
            if self._ngram_dict[key] == max_value:
                print ("{:d} -- {}".format(max_value, key))

def main():
    file = Input()
    wordlist = file.wordlist()
    n = Ngrams()
    n.process_wordlist(wordlist)
    n.print_max_ngrams()

main()
