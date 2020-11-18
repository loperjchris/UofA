"""
    File: phylo.py
    Author: Ruben Tequida
    Purpose: Reads in the FASTA file and creates a binary tree based on the
        genome sequence for each organism.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from genome import *
from tree import *
from sys import *

def main():
    """
    Reads in the FASTA file and the integer for N then calls functions to
        create the genome objects, index dictionary and the final tree.

    Parameters: None.

    Returns: None.
    """
    file = input("FASTA file: ")
    # Tests whether the input for N is an integer and quits if it isn't.
    try:
        N = int(input("n-gram size: "))
    except ValueError:
        print ("ERROR: Bad value for N")
        exit (1)
    # Trys to open the given file and quits if it can't.
    try:
        file = open(file)
    except FileNotFoundError:
        print ("ERROR: could not open file " + file)
        exit (1)
    data = file.read().split("\n")
    tree_list = create_tree_list(data, N)
    sim_dict = create_sim_dict(tree_list)
    make_tree(tree_list, sim_dict)

def create_tree_list(data, N):
    """
    Creates the list of all organism tree nodes that will construct the
        phylogenic tree.

    Parameters: data is a list and N is an integer.

    Returns: tree_list is a list.

    Pre-condition: data must be a list of the data gathered from the file
        supplied by the user.

    Post-condition: tree_list must be composed of Tree objects that hold the
        genome data of individual organisms.
    """
    tree_list = []
    seq = ""
    # Ignores a second newline at the end of the file if it exists.
    if data[-1] == data[-2] == "":
        data.pop()
    for line in data:
        # Grabs the name of the organism if the line begins with a >.
        if line != "":
            if line[0] == ">":
                line = line.split(" ")
                name = line[0][1:]
        # Creates the genome sequence of the organism until it hits an empty
            # line.
            else:
                seq += line
        # Once an empty newline is found creates a Tree object from the
            # GenomeData object, adds it to tree_list and resets seq.
        else:
            gd = GenomeData(name, seq, N)
            n = Tree(gd)
            n.add_genome(gd)
            tree_list.append(n)
            seq = ""
    return tree_list

def create_sim_dict(tree_list):
    """
    Creates a dictionary of the Jaccard indexes for each pair of organisms
        in the file given.

    Parameters: tree_list is a list.

    Returns: sim_dict is a dictionary.

    Pre-condition: tree_list must be composed of Tree objects that hold the
        genome data of individual organisms.

    Post-condition: sim_dict must be a dictionary of Jarccard indexes for each
        pair of organisms.
    """
    sim_dict = {}
    # Compares every organism with every other organism.
    for i in range(len(tree_list)):
        for j in range(i + 1, len(tree_list)):
            # Adds the pair's similarity to the dictionary.
            sim_dict[(tree_list[i].genome().name(), tree_list[j]\
                .genome().name())] = jaccard_index(tree_list[i]\
                .genome().ngrams(), tree_list[j].genome().ngrams())
    return sim_dict

def jaccard_index(seq1, seq2):
    """
    Finds the Jaccard index between two sets.

    Parameters: seq1 and seq2 are sets.

    Returns: A float.

    Pre-condition: seq1 and seq2 must be a set of all ngrams of a given
        length derived from the genome sequence.

    Post-condition: Should be a float that is between 0 and 1 inclusively.
    """
    i = seq1.intersection(seq2)
    u = seq1.union(seq2)
    return len(i) / len(u)

def make_tree(tree_list, sim_dict):
    """
    Creates the tree from each node in tree_list then prints it out.

    Parameters: tree_list is a list and sim_dict is a dictionary.

    Returns: None.

    Pre-condition: tree_list must be composed of Tree objects and sim_dict
        must hold all the Jaccard indexes for each pair of organisms.

    Post-condition: None.
    """
    # Continues putting tress together until only one tree is left in
        # tree_list.
    while len(tree_list) > 1:
        max_jindex = -1
        pos1 = -1
        pos2 = -1
        # Gets the similarity between each tree in tree_list and finds the
            # maximum similarity among all trees.
        for i in range(len(tree_list)):
            for j in range(i + 1, len(tree_list)):
                curr_jindex = find_jindex(tree_list[i],tree_list[j],sim_dict)
                if curr_jindex > max_jindex:
                    max_jindex = curr_jindex
                    pos1 = tree_list[i]
                    pos2 = tree_list[j]
        tree_list.remove(pos1)
        tree_list.remove(pos2)
        # Creates an empty Tree node and adds it to tree_list
        new_tree = Tree(GenomeData(None, None, None))
        if str(pos1) < str(pos2):
            new_tree.set_left(pos1)
            new_tree.set_right(pos2)
        else:
            new_tree.set_left(pos2)
            new_tree.set_right(pos1)
        # Keeping track of leaf nodes in each new tree.
        for genome in pos1.leaf_genomes():
            new_tree.add_genome(genome)
        for genome in pos2.leaf_genomes():
            new_tree.add_genome(genome)
        tree_list.append(new_tree)
    print (tree_list[0])

def find_jindex(n1, n2, sim_dict):
    """
    Finds the Jaccard index of two pairs of trees.

    Parameters: n1 and n2 are tree nodes and sim_dict is a dictionary.

    Returns: max_jindex is a float.

    Pre-condition: n1 and n2 must be Tree objects with all their leaf's
        genome sequence sets and sim_dict holds the pre-computed Jaccard
        index of the pairs of organisms in the tree.

    Post-condition: max_jindex must be a float between 0 and 1 inclusively.
    """
    max_jindex = 0
    # Compares each leaves genome set in n1 to those in n2.
    for genome1 in n1.leaf_genomes():
        for genome2 in n2.leaf_genomes():
            # Gets the index from the dictionary if the set is in the
                # dictionary and adds it if it isn't.
            if (genome1.name(), genome2.name()) in sim_dict:
                curr_jindex = sim_dict[(genome1.name(), genome2.name())]
            else:
                curr_jindex = jaccard_index(genome1.ngrams(),genome2.ngrams())
                sim_dict[(genome1.name(), genome2.name())] = curr_jindex
            max_jindex = max(max_jindex, curr_jindex)
    return max_jindex

main()
