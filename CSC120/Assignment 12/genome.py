"""
    File: genome.py
    Author: Ruben Tequida
    Purpose: Initializes a GenomeData object, creates the ngram set and
        returns GenomeData attributes.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
class GenomeData:

    def __init__(self, name, seq, N):
        """
        Initializes a GenomeData object and all of its attributes.

        Parameters: name and seq are strings and N is an integer.

        Returns: None.

        Pre-condition: name must be the name of the organism and seq must be
        a string of the genome sequence and N must be the ngram length.

        Post-condition: None.
        """
        self._name = name
        self._sequence = seq
        # Creates a set of ngrams if a new organism is be added to the
            #tree_list
        if N != None:
            self._ngrams = set([seq[x:x + N] for x in\
                range(len(seq) - N + 1)])
        # Sets ngrams to None if a non-leaf node is being added to the tree.
        else:
            self._ngrams = None

    def name(self):
        """
        Getter method for the name attribute.
        """
        return self._name

    def seq(self):
        """
        Getter method for the seq attribute.
        """
        return self._seq

    def ngrams(self):
        """
        Getter method for the ngrams attribute.
        """
        return self._ngrams

    def __str__(self):
        """
        Prints a GenomeData class object.

        Parameters: None.

        Returns: A string representation of a GenomeData name, sequence,
            and ngram set.
        """
        return "Name: {}, Sequence: {}, NGrams: {}"\
            .format(self._name, self._sequence, self._ngrams)
