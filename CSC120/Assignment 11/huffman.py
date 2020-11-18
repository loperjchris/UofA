"""
    File: huffman.py
    Author: Ruben Tequida
    Purpose: Creates a unique binary tree from an inorder and preorder list,
        which is then used to create a post order list and to decode a string
        of binary digits.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from sys import *

class BinaryTree:

    def __init__(self):
        """
        Initializes a BinaryTree class object and all of its attributes.

        Parameters: None.

        Returns: None.
        """
        self._value = None
        self._lchild = None
        self._rchild = None

    def build_tree(self, pre_order, in_order):
        """
        Recursively creates the binary tree from the preoder and inorder
            list.

        Parameters: pre_order and in_order are lists.

        Returns: None.

        Pre-condition: pre_order and in_order must be the lists to create a
            unique binary tree.

        Post-condition: None.
        """
        if pre_order != []:
            # Uses the first element of pre_order to determine the value of
                # the current node.
            root = pre_order[0]
            self._value = int(root)
            # Finds the index of root in the in_order list to find the left
                # and right parts of the binary tree.
            index = in_order.index(root)
            # If there are more elements to add to the tree a new BinaryTree
                # node will be created then the function will be called again
                # on modified lists.
            if pre_order[1:index + 1] != []:
                self._lchild = BinaryTree()
                self._lchild.build_tree(pre_order[1:index + 1],\
                    in_order[:index])
            if pre_order[index + 1:] != []:
                self._rchild = BinaryTree()
                self._rchild.build_tree(pre_order[index + 1:],\
                    in_order[index + 1:])

    def post_order(self):
        """
        Creates a string that is the post order traversal of the binary tree.

        Parameters: None.

        Returns: A string.

        Pre-condition: None.

        Post-condition: A string of integers separated by spaces.
        """
        # Returns the value when the node has no children.
        if self._lchild == self._rchild == None:
            return str(self._value) + " "
        # Proceeds only with left child if there is no right child.
        elif self._rchild == None:
            return self._lchild.post_order() + str(self._value) + " "
        # Proceeds only with right child if there is no left child.
        elif self._lchild == None:
            return self._rchild.post_order() + str(self._value) + " "
        # Traverses in post order: lchild, rchild, value.
        else:
            return self._lchild.post_order() + self._rchild.post_order()\
                + str(self._value) + " "

    def decode(self, seq, start):
        """
        Decodes a given string of binary integers and returns the decoded
            value.

        Parameters: seq is a list and start is a BinaryTree object.

        Returns: A string.

        Pre-condition: seq must be a list with each element being a 0 or a 1
            and start must be the root BinaryTree object.

        Post-condition: A string of values within leaf nodes that represent
            the unique sequence of 0's and 1's.
        """
        # Base case: gets the value of the last leaf in the sequence.
        if len(seq) == 1:
            if seq[0] == "0":
                return str(self._lchild._value)
            return str(self._rchild._value)
        # Determines whether to move left or right in the tree.
        if seq[0] == "0":
            if self._lchild != None:
                return self._lchild.decode(seq[1:], start)
        else:
            if self._rchild != None:
                return self._rchild.decode(seq[1:], start)
        return str(self._value) + start.decode(seq, start)


    def __str__(self):
        """
        Prints a BinaryTree class object.

        Parameters: None.

        Returns: A string representation of a BinaryTree and its children.
        """
        if self._value == None:
            return None
        else:
            return "({:d} {} {})".format(self._value, self._lchild, self._rchild)

def main():
    """
    Reads in the file, formats the data, and calls the functions necessary
        for the output.
    """
    data = read_data()
    bt = BinaryTree()
    bt.build_tree(data[0], data[1])
    print (bt.post_order().rstrip(" "))
    print (bt.decode(list(data[2].rstrip(" ")), bt))

def read_data():
    """
    Reads the file name and formats the data. Quits the program if it can't
        find the file.

    Parameters: None.

    Returns: file is a list.

    Pre-condition: None.

    Post-condition: file must be a properly formatted list with the first
        element being the preorder list and the second element being the
        inorder list and the last being the binary sequence to decode.
    """
    filename = input("Input file: ")
    # Opens the file given and quits the program if it can't find it.
    try:
        filename = open(filename)
    except FileNotFoundError:
        print ("ERROR: Could not open file " + filename)
        exit(1)
    file = filename.read().split("\n")
    file[0] = file[0].split(" ")
    file[1] = file[1].split(" ")
    return file

main()
