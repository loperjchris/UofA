"""
    File: friends.py
    Author: Ruben Tequida
    Purpose: Creates linked lists of individuals and their friends and prints
        a list of mutual friends between two individuals.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from linked_list import *
from sys import *

def main():
    """
    Reads in the data, initializes the linked list and calls a function to
        find mutual friends.
    """
    data = read_in()
    ll_friends = add_friends(data)
    mutual_friends(ll_friends)

def read_in():
    """
    Opens the given file and returns an error if it couldn't be found or
        opened.

    Parameters: None.

    Returns: filename is going to be a list of pairs of friends.
    """
    filename = input("Input file: ")
    try:
        filename = open(filename)
    # Prints an error and quits the program if the file cannot be opened.
    except FileNotFoundError:
        print ("ERROR: Could not open file " + filename)
        exit(1)
    return filename.read().split("\n")

def add_friends(data):
    """
    Adds each individual to the linked list and adds their friends to their
        friends linked list.

    Parameters: data is a list.

    Returns: ll_friends is a linked list.

    Pre-condition: data is a list composed of elements of two names gathered
        from a given text document.

    Post-condition: LL-friends is a linked list with each person in it and
        another linked list with their friends names.
    """
    ll_friends = LinkedList()
    # Formats each line so the list elements consist of two names and empty
        # elements are removed.
    for line in data:
        line = line.split(" ")
        names = []
        for element in line:
            if element != "":
                names.append(element)
        # Creates nodes out the names and adds them to the linked list as
            # well as adding their friends to the friends list.
        if names != []:
            friend1 = Node(names[0])
            friend2 = Node(names[1])
            ll_friends.add_name(friend1)
            ll_friends.add_name(friend2)
            ll_friends.add_friends(friend1, friend2)
            ll_friends.add_friends(friend2, friend1)
    return ll_friends

def mutual_friends(ll_friends):
    """
    Gets user input for two names and prints out the mutual friends the two
        names share.

    Parameters: ll_friends is a linked list.

    Returns: None.

    Pre-condition: ll_friends must be a completely created linked list with
        all names and friends added.

    Post-condition: None.
    """
    name1 = input("Name 1: ")
    name2 = input("Name 2: ")
    # Assertion that ends the program if either of the given names aren't
        # in the linked list.
    if not ll_friends.in_list(name1):
        print ("ERROR: Unknown person "  + name1)
        exit(1)
    if not ll_friends.in_list(name2):
        print ("ERROR: Unknown person "  + name2)
        exit(1)
    ll_friends.find_mutual_friends(name1, name2)

main()
