"""
    File: fake-news.py
    Author: Ruben Tequida
    Purpose: Prints out words in the given csv file that appear over a certain
        number of times.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from sys import *
import csv
import string

class Node:

    def __init__(self, word):
        """
        Initializes a Node object and all of its attributes.

        Parameters: word is a string.

        Returns: None.

        Pre-condition: word must be a string that is a single, pre-formatted
            word.

        Post-condition: None.
        """
        self._word = word
        self._count = 1
        self._next = None

    def word(self):
        """
        Getter method for the word of a Node object.
        """
        return self._word

    def count(self):
        """
        Getter method for the count of a Node object.
        """
        return self._count

    def next(self):
        """
        Getter method for the next attribute of a Node object.
        """
        return self._next

    def set_next(self, target):
        """
        Sets the next attribute to a new target.

        Parameters: target is a Node object.

        Returns: None.

        Pre-condition: target is a completely initialized Node object.

        Post-condition: None.
        """
        self._next = target

    def incr(self):
        """
        Increases the count of a Node object by 1.

        Parameters: None.

        Returns: None.
        """
        self._count += 1

    def __str__(self):
        """
        Creates a string representation of Node object.

        Parameters: None.

        Returns: A string that gives the word attribute and the count
            attribute of a Node object.
        """
        return self._word + " : " + str(self._count)

class LinkedList:

    def __init__(self):
        """
        Initializes a LinkedList object and all of its attributes.

        Parameters: None.

        Returns: None.
        """
        self._head = None

    def is_empty(self):
        """
        Determines if a linked list is empty or not.

        Parameters: None.

        Returns: True or False.
        """
        return self._head == None

    def head(self):
        """
        Getter method for the head of a LinkedList object.
        """
        return self._head

    def update_count(self, word):
        """
        Adds word to the linked list and creates a Node object for the word if
            it's not already in the linked list and increments the count if
            it is.

        Parameters: word is a string.

        Returns: None.

        Pre-condition: word must be a string that is a single, pre-formatted
            word.

        Post-condition: None.
        """
        current = self.head()
        # Adds the word to the linked list if it is empty.
        if current == None:
            new_node = Node(word)
            self.add(new_node)
        else:
            while current != None:
                # Increments the count of a word if it's already in the
                    # linked list.
                if current.word() == word:
                    current.incr()
                    break
                # Creates a new Node object and adds it to the linked list if
                    # it's not already in the linked list.
                elif current.next() == None:
                    new_node = Node(word)
                    self.add(new_node)
                    break
                else:
                    current = current.next()

    def rm_from_hd(self):
        """
        Removes the first Node from the linked list and returns it.

        Parameters: None.

        Returns: The removed node.
        """
        if self.is_empty():
            print ("ERROR: Linked List is empty")
        else:
            first = self.head()
            self._head = first.next()
            first._next = None
        return first

    def insert_after(self, node1, node2):
        """
        Inserts node2 after node1 in the linked list.

        Parameters: node1 and node2 are node objects.

        Returns: None.

        Pre-condition: node1 and node2 must be completely initialized node
            objects with next attributes.

        Post-condition: None.
        """
        node2.set_next(node1.next())
        node1.set_next(node2)

    def sort(self):
        """
        Sortes the linked list by the count of the node in descending order.

        Parameters: None.

        Returns: The completely sorted linked list.
        """
        ll_sorted = LinkedList()
        # Removes the first node from self and adds it to the empty ll_sorted
            # linked list.
        if self.head() != None:
            ll_sorted.add(self.rm_from_hd())
        # Continuously removes the first node from self and searches through
            # ll_sorted to find and add it to its correct position in
            # descending count order.
        while not self.is_empty():
            to_sort = self.rm_from_hd()
            current = ll_sorted.head()
            # Adds the node to the front of the list if the head points to
                # a node with a smaller count.
            if current.count() < to_sort.count():
                ll_sorted.add(to_sort)
            else:
                while current != None:
                    # If the next node is None or smaller than to_sort then
                        # adds it to ll_sorted and goes to sort the next node.
                    if current.next() == None or current.next().count()\
                        < to_sort.count():
                        ll_sorted.insert_after(current, to_sort)
                        break
                    current = current.next()
        # Sets the head of self to point to the new sorted list.
        self._head = ll_sorted.head()
        return self

    def get_nth_highest_count(self, n):
        """
        Finds the node at postion n and returns its count.

        Parameters: n is an integer.

        Returns: current.count() is a node's count attribute.

        Pre-condition: n must be an integer between 0 and the length of the
            linked list.

        Post-condition: current must be a Node object and its count attribute
            must be set and properly incremented.
        """
        current = self.head()
        # Finds the nth element by looping through the linked list and
            # stopping at n.
        for place in range(n):
            if current.next() == None:
                count = -1
                break
            else:
                current = current.next()
                count = current.count()
        return count

    def print_upto_count(self, n):
        """
        Prints out each node in the linked list that has a count higher or
            equal to n.

        Parameters: n is an integer.

        Returns: None.

        Pre-condition: n is an integer that must be determined by the
            get_nth_highest_count method.

        Post-condition: None.
        """
        current = self.head()
        value = self.head().count()
        while value >= n:
            print ("{} : {:d}".format(current.word(), current.count()))
            current = current.next()
            value = current.count()

    def add(self, node):
        """
        Adds a node to the front of a linked list.

        Parameters: node is a Node object.

        Returns: None.

        Pre-condition: node must be a completely initialized Node object.

        Post-condition: None.
        """
        node.set_next(self.head())
        self._head = node

    def __str__(self):
        """
        Creates a string representation of a LinkedList object.

        Parameters: None.

        Returns: ll_string is a string with each node of the linked list
            printed out and pointing to the next node.
        """
        ll_string = "Head ->\n"
        current = self.head()
        # Goes through each node of the linked list and adds the string
            # representation of the node to ll_string.
        while current != None:
            ll_string += str(current) + " ->\n"
            current = current.next()
        ll_string += "None"
        return ll_string

def main():
    """
    Starts the functions to open the file, format its contents, and create,
        sort, and print its contents.

    Parameters: None.

    Returns: None.
    """
    file = open_file()
    word_list = format_file(file)
    word_ll = feed(word_list)
    return_words(word_ll)


def open_file():
    """
    Determines if the file can be opened and returns csv formatted list.

    Parameters: None.

    Returns: A list of the files contents in a csv reader format.
    """
    filename = input("File: ")
    try:
        filename = open(filename)
    # Prints and error and quits the program if the file cannot be opened.
    except FileNotFoundError:
        print ("ERROR: Could not open file " + filename)
        exit(1)
    return csv.reader(filename)

def format_file(file):
    """
    Formats the desired content of the file in a way that makes it usable to
        the program.

    Parameters: file is a list.

    Returns: word_list is a list.

    Pre-condition: file is a list containing all the information from the csv
        file.

    Post-condition: word_list is a list of the words that need to be added
        to a linked list.
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

def feed(word_list):
    """
    Adds all the words in to a linked list and then sorts it.

    Parameters: word_ll is a list.

    Returns: word_list is a linked list.

    Pre-condition: word_list must be list with each element being a single,
        pre-formatted word.

    Post-condition: word_ll is a linked list made of nodes for each word and
        has been sorted.
    """
    word_ll = LinkedList()
    for word in word_list:
        word_ll.update_count(word)
    return word_ll.sort()

def return_words(word_ll):
    """
    Gets an integer from the user and uses that number to find the element
        in the linked list at that position and get its count, then prints
        all the words with an equal or higher count.

    Parameters: word_ll is a linked list.

    Returns: None.

    Pre-condition: word_ll is a linked list with all of its nodes in
        descending order by count.

    Post-condition: None.
    """
    try:
        N = int(input("N: "))
    # Prints and error and quits the program if N isn't an integer.
    except ValueError:
        print ("ERROR: Could not read N")
        exit(1)
    # Asserts whether N is a positive number.
    assert N >= 0
    N_count = word_ll.get_nth_highest_count(N)
    if N_count > 0:
        word_ll.print_upto_count(N_count)

main()
