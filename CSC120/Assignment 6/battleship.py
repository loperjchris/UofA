"""
    File: battleship.py
    Author: Ruben Tequida
    Purpose: Creates a battleship board with ship placement for one player
        and guesses on those ships.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from sys import *

class GridPos:

    def __init__(self, x, y):
        """
        Initializes a GridPos object and all of its attributes.

        Parameters: x and y are integers.

        Returns: None.

        Pre-condition: x and y are greater than -1 and less than 10.

        Post-condition: None.
        """
        self._coord = [x, y]
        self._ship = None
        self._guessed = False

    def update_ship(self, ship):
        """
        Updates the grid positions for a placed ship.

        Parameters: ship is a Ship class object.

        Returns: None.

        Pre-condition: ship must be a Ship class object.

        Post-condition: None.
        """
        # Checks to see if there is already a ship where the new ship is
            #to be placed and throws an error and quits if there is.
        if self._ship != None:
            print ("ERROR: overlapping ship: " + ship._name + " "\
                + str(ship._start[0]) + " " + str(ship._start[1]) + " "\
                + str(ship._end[0]) + " " + str(ship._end[1]) + "\n")
            exit(1)
        self._ship = ship

    def update_pos(self, board):
        """
        Updates the grid at the position of each guess.

        Parameters: board is a Board class object.

        Returns: None.

        Pre-condition: board must be a completed Board class object.

        Post-condition: None.
        """
        # Determines the output if the guessed position is empty.
        if self._ship == None:
            if not self._guessed:
                print ("miss")
                self._guessed = True
            else:
                print ("miss (again)")
        # Determines the output if there is a ship at the guessed position.
        else:
            if self._guessed:
                print ("hit (again)")
            else:
                self._guessed = True
                self._ship._hits += 1
                # Determines if the ship is sunk
                if self._ship._hits == self._ship._size:
                    board._ships_sunk += 1
                # If a ship is sunk with this guess, prints out the ship.
                if self._ship._hits == self._ship._size:
                    print ("{} sunk".format(self._ship._name))
                else:
                    print ("hit")
                # Ends the game if all ships have been sunk
                if board._ships_sunk == 5:
                    print ("all ships sunk: game over")
                    exit(1)

    def is_empty(self):
        """
        Determines if the grid position is empty or not.

        Parameters: None.

        Returns: True or False.
        """
        if self._ship == None:
            return True
        return False

    def is_guessed(self):
        return self._guessed

    def get_type(self):
        return self._ship._name

    def __str__(self):
        """
        Creates a string that's printed when print is called on a GridPos
            object.

        Parameters: None

        Returns: A string with the grid coordinates, name of the ship at that
            position and whether it's been guessed or not.
        """
        return "Coordinates: (" + str(self._coord[0]) + ","\
            + str(self._coord[1]) + "), Name: " + str(self._ship)\
            + ", Guessed: " + str(self._guessed)

class Board:

    def __init__(self):
        """
        Initializes a Board object and all of its attributes including an
            empty dictionary for ship objects.

        Parameters: None.

        Returns: None.
        """
        self._board = []
        # Creates an empty 10 x 10 board
        for x in range(10):
            self._board.append([])
            for y in range(10):
                self._board[x].append(GridPos(x, y))
        self._collection = {}
        self._ships_sunk = 0

    def add_ship(self, ship):
        """
        Adds a ship object to the board.

        Parameters: ship is a Ship class object.

        Returns: None.

        Pre-condition: ship must be a Ship class object.

        Post-condition: None.
        """
        # Calls update_ship for every position the newly added ship is
            # going to occupy
        for x in range(ship._start[0], ship._end[0] + 1):
            for y in range(ship._start[1], ship._end[1] + 1):
                self._board[x][y].update_ship(ship)
        self._collection[ship._name] = ship

    def update_board(self, x, y):
        """
        Updates the board for a given guess.

        Parameters: x and y are integers.

        Returns: None.

        Pre-condition: x and y must be between -1 and 10.

        Post-condition: None.
        """
        self._board[x][y].update_pos(self)

    def __str__(self):
        """
        Creates a string representation of the board.

        Parameters: None.

        Returns: A string that shows the board with empty, ship, and hit
            spaces.
        """
        grid = ""
        for row in self._board:
            for column in row:
                # Determines what to print if the grid position is empty,
                    # contains a ship, or has been hit.
                if not column.is_empty():
                    if column.is_guessed():
                        grid += "H "
                    else:
                        grid += str(column.get_type()) + " "
                else:
                    grid += "~ "
            grid += "\n"
        return grid


class Ship:

    def __init__(self, name, x1, y1, x2, y2):
        """
        Initializes a Ship object and all of it's attributes.

        Parameters: name is a string, x1, y1, x2, and y2 are integers.

        Returns: None.

        Pre-condition: name must be a valid ship name, x1, y1, x2, and y2
            must be valid ship placement positions.

        Post-condition: None.
        """
        self._name = name
        # Swaps the y positions if y1 is greater than y2 for computing
            # purposes.
        if y1 > y2:
            temp = y1
            y1 = y2
            y2 = temp
        self._start = [x1, y1]
        self._end = [x2, y2]
        # Determines the length of the ship.
        if x1 == x2:
            length = abs(y1 - y2) + 1
        else:
            length = abs(x1 - x2) + 1
        self._size = length
        self._hits = 0

    def __str__(self):
        """
        Creates a string representation of a Ship object.

        Parameters: None.

        Returns: A string that shows the ship name and its placement position.
        """
        return self._name + " " + str(self._start[0]) + " "\
            + str(self._start[1]) + " " + str(self._end[0]) + " "\
            + str(self._end[1])


def main():
    """
    Checks and opens files and creates class objects for the game.

    Parameters: None.

    Returns: None.
    """
    placement = input()
    guesses = input()
    # Tests whether the given files exist or not.
    try:
        placement = open(placement)
    except FileNotFoundError:
        print ("ERROR: Could not open file: " + placement)
        exit(1)
    try:
        guesses = open(guesses)
    except FileNotFoundError:
        print ("ERROR: Could not open file: " + guesses)
        exit(1)
    placement = placement.read().split("\n")
    guesses = guesses.read().split("\n")
    error_check(placement)
    board = start_game(placement)
    play(guesses, board)

def error_check(placement):
    """
    Checks for ship placement errors and exits the program if any are found.

    Parameters: placement is list of ship placements.

    Returns: None.

    Pre-condition: each element of placement must have a ship name and a
        beginning and ending set of coordinates.

    Post-condition: None.
    """
    if placement[-1] == "":
        del placement[-1]
    for line in placement:
        line = line.split(" ")
        # Checks if the given ship names are valid and if there are exaclty
            # 5 ships given.
        if line[0] not in "ABSDP" or len(placement) != 5:
            print ("ERROR: fleet composition incorrect")
            exit(1)
        # Checks if the given ship will fall out of bounds.
        for num in line[1:]:
            if not 0 <= int(num) <= 9:
                print ("ERROR: ship out-of-bounds: " + " ".join(line) + "\n")
                exit(1)
        # Checks if the ship is not horizontal or vertical.
        if line[1] != line[3] and line[2] != line[4]:
            print ("ERROR: ship not horizontal or vertical: "\
                + " ".join(line) + "\n")
            exit(1)
        # Finds the length of the ship
        if line[1] == line[3]:
            length = abs(int(line[2]) - int(line[4])) + 1
        else:
            length = abs(int(line[1]) - int(line[3])) + 1
        # Checks if the length of the ship matches with the name of the ship.
        if line[0] == "A" and length != 5 or line[0] == "B" and length != 4 \
            or line[0] == "S" and length != 3 or line[0] == "D" and length \
            != 3 or line[0] == "P" and length != 2:
            print ("ERROR: incorrect ship size: " + " ".join(line) + "\n")
            exit(1)

def start_game(placement):
    """
    Places each ship on the board.

    Parameters: placement is a list of ship placements.

    Returns: board is a Board class object.

    Pre-condition: each element of placement must have a ship name and a
        beginning and ending set of coordinates.

    Post-condition: board must be completely initializes and have all ships
        placed.
    """
    board = Board()
    # Creates a Ship object and adds the ship to the board.
    for line in placement:
        line = line.split(" ")
        ship = Ship(line[0], int(line[1]), int(line[2]), int(line[3]), int(line[4]))
        board.add_ship(ship)
    return board

def play(guesses, board):
    """
    Evaluates the outcome of each guess.

    Parameters: guesses is a list of guesses and board is a Board object.

    Returns: None.

    Pre-condition: guesses elements must be two integers and the board must
        be a completely initialized Board object.

    Post-condition: None.
    """
    for guess in guesses:
        guess = guess.split(" ")
        # Skips the guess if it isn't valid.
        if len(guess) != 2 or not 0 <= int(guess[0]) <= 9 or\
            not 0 <= int(guess[1]) <= 9:
            print ("illegal guess")
        else:
            board.update_board(int(guess[0]), int(guess[1]))

main()
