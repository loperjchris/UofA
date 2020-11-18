"""
    File: pokemon.py
    Author: Ruben Tequida
    Purpose: Compute the maximum average values for various Pokemon attributes
        and print the max average of the attribute queried.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
def main():
    pokemon_dictionary = create_pokemon_dictionary()
    max_averages = find_averages(pokemon_dictionary)
    print_averages(max_averages)

def print_averages(max_averages):
    """
    Reads in query and prints out the max average associated with the queried
        attribute.

    Parameters: max_averages is a dictionary of attributes and max averages.

    Returns: None.
    """
    query = input()
    while query != '':
        query = query.lower()
        # modify query to match the values in max_averages
        if query == "hp":
            query = "HP"
        elif query == "specialattack":
            query = "Sp. Atk"
        elif query == "specialdefense":
            query = "Sp. Def"
        else:
            query = query.title()
        # pulls specified data from max_averages, sorts it, then prints it
        if query in max_averages:
            to_print = []
            for key, value in max_averages[query].items():
                to_print.append([key, value])
            to_print.sort()
            for value in to_print:
                print ("{}: {}".format(value[0], value[1]))
        query = input()

def create_pokemon_dictionary():
    """
    Establishes the dictionary with totals of each attribute for each type.

    Parameters: None.

    Returns: pokemon_dictionary is a dictionary with the attribute totals.
    """
    data_file = open(input())
    file = data_file.read().split("\n")
    data_file.close()
    del file[-1]
    structure = file.pop(0).split(",")
    del structure[0:4] # remove unused attributes
    del structure[7:9] # remove unused attributes
    pokemon_dictionary = {}
    # populate dictionary with the sum of each attribute for each type
    for line in file:
        row = line.split(",")
        type1 = row.pop(2)
        del row[0:3]
        del row[7:9]
        place = 0
        # create dictionary entries for types not yet created
        if type1 not in pokemon_dictionary:
            pokemon_dictionary[type1] = {}
            for i in range(len(structure)):
                pokemon_dictionary[type1][structure[i]] = 0
            pokemon_dictionary[type1]["count"] = 0
        for item in row:
            pokemon_dictionary[type1][structure[place]] += int(item)
            place += 1
        pokemon_dictionary[type1]["count"] += 1
    return pokemon_dictionary

def find_averages(pokemon_dictionary):
    """
    Creates a dictionary with the max averages for each attribute.

    Parameters: pokemon_dictionary must be populated with attribute totals.

    Returns: max_averages is a dictionary of the max averages for each
        attribute.
    """
    # change attribute totals into averages
    for type in pokemon_dictionary:
        for attribute in pokemon_dictionary[type]:
            pokemon_dictionary[type][attribute] = (
                pokemon_dictionary[type][attribute] /
                pokemon_dictionary[type]["count"])
    max_averages = (
        {"Total":{}, "HP":{}, "Attack":{}, "Defense":{},
         "Sp. Atk":{}, "Sp. Def":{}, "Speed":{}})
    # create another dictionary with the type for each max average
    for attribute in max_averages:
        max_value = 0
        #find the maximum average
        for type in pokemon_dictionary:
            if pokemon_dictionary[type][attribute] > max_value:
                temp_dict = {}
                max_value = pokemon_dictionary[type][attribute]
                temp_dict[type] = max_value
            elif pokemon_dictionary[type][attribute] == max_value:
                temp_dict[type] = max_value
            max_averages[attribute] = temp_dict
    return max_averages

main()
