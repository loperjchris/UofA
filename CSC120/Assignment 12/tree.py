"""
    File: tree.py
    Author: Ruben Tequida
    Purpose: Initializes a Tree object, returns Tree attributes and creates
        the structure of the tree by setting the left and right children.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from genome import *

class Tree:

    def __init__(self, genome):
        """
        Initializes a Tree class object and all of its attributes.

        Parameters: genome is a GenomeData object.

        Returns: None.

        Pre-condition: genome must be a completely initialized GenomeData
            object.

        Post-condition: None.
        """
        self._genome = genome
        self._name = genome.name()
        self._left = None
        self._right = None
        self._leaf_genomes = []

    def is_leaf(self):
        """
        Determines if the given tree node is a leaf or not.

        Parameters: None.

        Returns: True or False.
        """
        return self._left == self._right == None

    def add_genome(self, genome):
        """
        Adds a GenomeData object to the self._genome attribute.

        Parameters: genome is a GenomeData object.

        Returns: None.

        Pre-condition: genome must be a completely initialized GenomeData
            object.

        Post-condition: None.
        """
        self._leaf_genomes.append(genome)

    def set_left(self, node):
        """
        Setter method for self._left attribute.
        """
        self._left = node

    def set_right(self, node):
        """
        Setter method for self._right attribute.
        """
        self._right = node

    def genome(self):
        """
        Getter method for the genome attribute.
        """
        return self._genome

    def name(self):
        """
        Getter method for the name attribute.
        """
        return self._name

    def left(self):
        """
        Getter method for the left attribute.
        """
        return self._left

    def right(self):
        """
        Getter method for the right attribute.
        """
        return self._right

    def leaf_genomes(self):
        """
        Getter method for the leaf_genomes attribute.
        """
        return self._leaf_genomes

    def __str__(self):
        """
        Prints a Tree class object.

        Parameters: None.

        Returns: A string representation of a Tree name and its left and right
            children.
        """
        if self.is_leaf():
            return self.name()
        else:
            return "({}, {})".format(str(self.left()), str(self.right()))
