"""
    File: street.py
    Author: Ruben Tequida
    Purpose: Creates a visualization of a street that includes buildings,
        parks and empty lots.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
class Building:

    def __init__(self, form):
        """
        Initializes a Building class object and all of its attributes.

        Parameters: form is a string.

        Returns: None.

        Pre-condition: form is a string with the first number being the width,
            the second number being the height and the final character
            is the brick.

        Post-condition: None.
        """
        form = form.split(",")
        self._width = int(form[0])
        self._height = int(form[1])
        self._brick = form[2]
        # Structure is going to be the representation of the building with
            # each element being a different level.
        self._structure = []
        self.build(self._width, self._height, self._brick)

    def build(self, w, h, b):
        """
        Recursively creates each level of the building.

        Parameters: w and h are integers and b is a string.

        Returns: None.

        Pre-condition: w is the width of the building, h is the height of the
            street currently being printed, and b is the character the
            building is made of.

        Post-condition: None.
        """
        if h != 0:
            self._structure.append(b * w)
            self.build(w, h - 1, b)

    def print_structure(self, h):
        """
        Prints the structure element at index h and prints blank spaces if h
            is taller than the building.

        Parameters: h is an integer.

        Returns: the element at index h or a string of blank spaces if h is
            taller than the building.

        Pre-condition: h must be a positive integer greater than or equal to
            zero.

        Post-condition: must return a string equal in length to the width of
            the building.
        """
        if h >= self._height:
            return " " * self._width
        else:
            return self._structure[h]

    def width(self):
        """
        Getter method for width attribute.
        """
        return self._width

    def height(self):
        """
        Getter method for height attribute.
        """
        return self._height

    def brick(self):
        """
        Getter method for brick attribute.
        """
        return self._brick

    def structure(self):
        """
        Getter method for structure attribute.
        """
        return self._structure

    def __str__(self):
        """
        Prints a Building class object.

        Parameters: None.

        Returns: A string representation of a building's form and structure.
        """
        return "b:" + str(self._width) + "," + str(self._height) + ","\
            + self._brick + ": " + str(self._structure)

class Park:

    def __init__(self, form):
        """
        Initializes a Park class object and all of its attributes.

        Parameters: form is a string.

        Returns: None.

        Pre-condition: form is a string with the first number being the width,
            and the final character is the foliage.

        Post-condition: None.
        """
        form = form.split(",")
        self._width = int(form[0])
        self._foliage = form[1]
        self._structure = []
        # Parks will always have a height of 5.
        self.build(self._width, self._foliage, 5)

    def build(self, w, f, h):
        """
        Recursively creates each level of the park.

        Parameters: w and h are integers and f is a string.

        Returns: None.

        Pre-condition: w is the width of the park, h is the height of the
            street currently being printed, and f is the character the
            tree in the park is made of.

        Post-condition: None.
        """
        # Builds the tree backwards for printing purposes.
        if h != 0:
            # Uses the variable half to determine the number of spaces to put
                # on either side of the tree.
            half = w // 2
            # Adds the foliage of the tree with spaces on the sides.
            if h == 1:
                self._structure.append(" " * half + f + " " * half)
            elif h == 2:
                self._structure.append(" " * (half - 1) + f * 3 + " "\
                    * (half - 1))
            elif h == 3:
                self._structure.append(" " * (half - 2) + f * 5 + " "\
                    * (half - 2))
            # Adds the trunk of the tree with spaces on the sides.
            else:
                self._structure.append(" " * half + "|" + " " * half)
            self.build(w, f, h - 1)

    def print_structure(self, h):
        """
        Prints the structure element at index h and prints blank spaces if h
            is greater than or equal to 5.

        Parameters: h is an integer.

        Returns: the element at index h or a string of blank spaces if h is
            greater than or equal to 5.

        Pre-condition: h must be a positive integer greater than or equal to
            zero.

        Post-condition: must return a string equal in length to the width of
            the park.
        """
        if h >= 5:
            return " " * self._width
        else:
            return self._structure[h]

    def width(self):
        """
        Getter method for width attribute.
        """
        return self._width

    def foliage(self):
        """
        Getter method for foliage attribute.
        """
        return self._foliage

    def structure(self):
        """
        Getter method for structure attribute.
        """
        return self._structure

    def __str__(self):
        """
        Prints a Park class object.

        Parameters: None.

        Returns: A string representation of a park's form and structure.
        """
        return "p:" + str(self._width) + "," + self._foliage + ": "\
            + str(self._structure)

class Empty:

    def __init__(self, form):
        """
        Initializes an Empty class object and all of its attributes.

        Parameters: form is a string.

        Returns: None.

        Pre-condition: form is a string with the first number being the width,
            and the final character is the composition of trash.

        Post-condition: None.
        """
        form = form.split(",")
        self._width = int(form[0])
        self._trash = form[1]
        self._trash = self._trash.replace("_", " ")
        # Structure is only a string since all empty lots will be of height
            # 1.
        self._structure = ""
        self.build(self._width, self._trash)

    def build(self, w, t):
        """
        Recursively creates an empty lot by repeating the trash string until
            it reaches the desired width.

        Parameters: w is an integers and t is a string.

        Returns: None.

        Pre-condition: w is the width of the empty lot, and t is the string
            that is repeated until it reaches the width.

        Post-condition: None.
        """
        # Cuts off the end of t if it exceeds the length of the given width.
        if w <= len(t):
            self._structure += t[:w]
        else:
            self._structure += t
            self.build(w - len(t), t)

    def print_structure(self, h):
        """
        Prints the structure element at level 0 and prints blank spaces if h
            is greater than or equal to 1.

        Parameters: h is an integer.

        Returns: the the string at level 0 or a string of blank spaces if h is
            greater than or equal to 1.

        Pre-condition: h must be a positive integer greater than or equal to
            zero.

        Post-condition: must return a string equal in length to the width of
            the empty lot.
        """
        if h >= 1:
            return " " * self._width
        else:
            return self._structure

    def width(self):
        """
        Getter method for width attribute.
        """
        return self._width

    def trash(self):
        """
        Getter method for trash attribute.
        """
        return self._trash

    def structure(self):
        """
        Getter method for structure attribute.
        """
        return self._structure

    def __str__(self):
        """
        Prints an Empty class object.

        Parameters: None.

        Returns: A string representation of an empty lot's form and structure.
        """
        return "e:" + str(self._width) + "," + self._trash + ": "\
            + str(self._structure)

class Street:

    def __init__(self):
        """
        Initializes a Street class object and all of its attributes.

        Parameters: None.

        Returns: None.
        """
        self._street = []
        self._height = 0
        self._width = 0

    def add(self, item):
        """
        Adds a building, park, or empty lot object to the street attribute.

        Parameters: item is a building, park, or empty object.

        Returns: None.

        Pre-condition: item must be a completely initialized building, park,
            or empty object.

        Post-condition: None.
        """
        self._street.append(item)

    def check_height(self, object_height):
        """
        Changes the overall street height if a empty lot, park, or building
            exceeds the current height.

        Parameters: object_height is an integer.

        Returns: None.

        Pre-condition: object_height must be an integer that is the height
            of the object being added to the street.

        Post-condition: None.
        """
        if object_height > self.height():
            self._height = object_height

    def incr_width(self, value):
        """
        Increases the width for every object added to the street.

        Parameters: value is an integer.

        Returns: None.

        Pre-condition: value must be an integer that is the width of the
            object being added to the street.

        Post-condition: None.
        """
        self._width += value

    def print_street(self, h):
        """
        Prints out the border of the street and all of the objects within it.

        Parameters: h is an integer.

        Returns: None.

        Pre-condition: h must start at the max height of the street.

        Post-condition: None.
        """
        # Prints the bottom border.
        if h == -1:
            print ("+" + "-" * self.width() + "+")
        # Prints the top border and space between the top border and the
            # highest object on the street.
        elif h == self.height():
            print ("+" + "-" * self.width() + "+")
            print ("|" + " " * self.width() + "|")
            self.print_street(h - 1)
        # Prints the borders with the street objects inbetween.
        else:
            print ("|" + self.on_street(self._street, h) + "|")
            self.print_street(h - 1)

    def on_street(self, lst, h):
        """
        Recursively prints out each object on the street at the given level.

        Parameters: lst is a list and h is an integer.

        Returns: a string.

        Pre-condition: lst is a list of all the objects on the street and h
            is an integer that begins at the height of the tallest object.

        Post-condition: the string is composed of all the object elements at
            that level and must equal the overall width of the street.
        """
        if lst == []:
            return ""
        else:
            return lst[0].print_structure(h) + self.on_street(lst[1:] , h)

    def get_street(self):
        """
        Getter method for the street attribute.
        """
        return self._street

    def height(self):
        """
        Getter method for the height attribute.
        """
        return self._height

    def width(self):
        """
        Getter method for the width attribute.
        """
        return self._width

    def __str__(self):
        """
        Prints a Street class object.

        Parameters: None.

        Returns: A string representation of a street.
        """
        return "Height: " + str(self._height) + ", Width: "\
            + str(self._width) + ", Street: " + str(self._street)

def main():
    """
    Reads in the data, constructs the street and all of its objects, and then
        prints them out.
    """
    street_s = input("Street: ")
    street_l = street_s.split()
    street = Street()
    read_street(street_l, street)
    street.print_street(street.height())

def read_street(s_list, street):
    """
    Recursively creates and adds objects to the street from the given input
        string.

    Parameters: street_l is a list and street is a Street object.

    Returns: None.

    Pre-condition: street_l is a list of the input string split by object and
        street is an empty, initialized Street object.

    Post-condition: None.
    """
    if s_list != []:
        # Creates a Park object and adds it to street.
        if s_list[0][0] == "p":
            p = Park(s_list[0][2:])
            street.add(p)
            # Checks the height to see if it's less than 5.
            street.check_height(5)
            street.incr_width(p.width())
        # Creates a Building object and adds it to street.
        if s_list[0][0] == "b":
            b = Building(s_list[0][2:])
            street.add(b)
            # Changes the street height only if the width is greater than 0.
            if b.width() != 0:
                street.check_height(b.height())
            # Changes the street width only if the height is greater than 0.
            if b.height() != 0:
                street.incr_width(b.width())
        # Creates an Empty object and adds it to street.
        if s_list[0][0] == "e":
            e = Empty(s_list[0][2:])
            street.add(e)
            # Only changes street height if the height is greater than 0.
            if e.width() != 0:
                street.check_height(1)
            street.incr_width(e.width())
        # Progresses to the next object.
        read_street(s_list[1:], street)

main()
