"""
    File: rhymes.py
    Author: Ruben Tequida
    Purpose: Print a list of words (derived from a pronunciation dictionary)
        that rhyme with a user given word.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
def main():
    pronunciation_dictionary = create_dictionary()
    does_it_rhyme(pronunciation_dictionary)

def create_dictionary():
    """
    Reads in the pronunciation dictionary file and populates a dictionary
        with the word as the key and a list of lists filled with phonemes.

    Parameters: none.

    Returns: word_dict is a dictionary with all the words and their
        pronunciations.
    """
    file = open(input())
    data = file.read().split("\n")
    file.close()
    word_dict = {}
    # Adds each word to the word_dict as a key and the values are lists of
        # the phonemes
    for line in data:
        line = line.split()
        for place in range(len(line)):
            if place == 0:
                current_word = line[place]
                if current_word not in word_dict:
                    word_dict[current_word] = []
                    word_dict[current_word].append([])
                    count = 0
                else:
                    # count is used to keep track of how many pronunciations
                        # there are
                    count += 1
                    word_dict[current_word].append([])
            else:
                word_dict[current_word][count].append(line[place])
    return word_dict

def does_it_rhyme(pronunciation_dictionary):
    """
    Compares the given word to word_dict to see which words rhyme.

    Parameters: pronunciation_dictionary must be populated with words
        and their pronunciations.

    Returns: None.
    """
    word = input().upper()
    rhyme_list = []
    if word in pronunciation_dictionary:
        # Grabs the pronunciation of the word given
        for pronunciation in pronunciation_dictionary[word]:
            pattern1 = find_pattern(pronunciation, pronunciation_dictionary)
            # Grabs the pronunciation of every word in word_dict and compares
                # it to the given word
            for dict_word in pronunciation_dictionary:
                for dict_pronunciation in pronunciation_dictionary[dict_word]:
                    pattern2 = find_pattern(dict_pronunciation, pronunciation_dictionary)
                    if pattern2 != None:
                        # Checks if both perfect rhyme rules are met and skips
                            # the given word
                        if pattern1[1:] == pattern2[1:] and pattern1 != pattern2 and word != dict_word:
                            rhyme_list.append(dict_word)
    for rhyme in rhyme_list:
        print (rhyme)

def find_pattern(pronunciation, pronunciation_dictionary):
    """
    Determines the rhyming portion of the word for comparison by finding the
        primary stressor.

    Parameters: pronunciation is a list of phonemes and
        pronunciation_dictionary is the word list to compare it to.

    Returns: pattern is a list of phonemes for the part of the word to rhyme
        with.
    """
    pattern = []
    for place in range(len(pronunciation)):
        if pronunciation[place][-1:] == "1":
            # Handles words that begin with the primary stressor by adding an
                # element to the front of the pattern
            if place == 0:
                pattern = (pronunciation[place:])
                pattern.insert(0, "")
            else:
                pattern = pronunciation[place - 1:]
            return pattern

main()
