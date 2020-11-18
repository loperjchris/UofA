"""
    File: dates.py
    Author: Ruben Tequida
    Purpose: Prints events to the corresponding date that is given.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
class Date:

    def __init__(self, date, event):
        """
        Initializes date objects with date and event attributes.

        Parameters: date and event are a string.

        Returns: None.
        """
        self._date = date
        self._event = [event]

    def add_event(self, event):
        """
        Adds an event to the event list attribute of Date objects.

        Parameters: event is a string.

        Returns: None.
        """
        self._event.append(str(event))

    def get_event(self):
        return self._event

    def __str__(self):
        """
        Creates a string that's printed when print is called on a Date Object.

        Parameters: A date class object.

        Returns: A string representation of Date objects.
        """
        i = 1
        event_str = ""
        # Runs throught the event list in order to print every event
            #separated by a comma.
        for event in self._event:
            if i != len(self._event):
                event_str += event + ", "
                i += 1
            else:
                event_str += event
        return str(self._date) + ": " + event_str

class DateSet:

    def __init__(self):
        """
        Initializes a DateSet object.

        Parameters: None.

        Returns: None.
        """
        self._dates = {}

    def add_date(self, date):
        """
        Adds a Date object to the DateSet dictionary object.

        Parameters: date is a Date object.

        Returns: None.
        """
        self._dates[date._date] = date

    def get_dict(self):
        return self._dates

    def get_date(self, date):
        return self._dates[date]

    def __str__(self):
        """
        Creates a string that's printed when print is called on a DateSet
            Object.

        Parameters: A DateSet object.

        Returns: A string representation of a DateSet object dictionary.
        """
        dict_str = "{"
        i = 1
        # Prints the DateSet dictionary object as a visual representation
            # of a dictionary.
        for key in self._dates:
            if i != len(self._dates):
                dict_str += "'" + key + "': '" + str(self._dates[key]) + "',"
                i += 1
            else:
                dict_str += "'" + key + "': '" + str(self._dates[key]) + "'}"
        return dict_str

def main():
    set_dates()

def set_dates():
    """
    Creates Date objects from the given dates and fills the DateSet dictionary
        with the Date objects.

    Parameters: none.

    Returns: None.
    """
    file = open(input())
    data = file.read().split("\n")
    file.close()
    # Initializing the DateSet dictionary object
    dates = DateSet()
    # Runs through the text and formats the date and event lines for
        # processing
    for line in data:
        if line != "":
            # Grabs the operator to determine what to do with the following
                # line of text.
            operator = line[0][0]
            # Throws an assertion error if the line doesn't begin with an I
                # or an R.
            assert operator == "I" or operator == "R", \
                "ERROR: Illegal operation."
            line = line.split(":", 1)
            date_line = line[0].strip("I ").strip("R ")
            date_line = format_date(date_line)
            if operator == "I":
                event_line = line[1].strip(" ")
                # Processes the information depending if the date already
                    # has an associated Date object.
                if date_line not in dates.get_dict():
                    date1 = Date(date_line, event_line)
                    dates.add_date(date1)
                else:
                    date1 = dates.get_dict()[date_line]
                    date1.add_event(event_line)
                    dates.add_date(date1)
            else:
                # Prints out the date and events of a date request.
                if date_line in dates.get_dict():
                    date_object = dates.get_date(date_line)
                    events = date_object.get_event()
                    events.sort()
                    for event in events:
                        print ("{}: {}".format(date_line, event))

def format_date(date):
    """
    Formats the date into a canonical representation.

    Parameters: date is a string representation of a date.

    Returns: A canonical representation of the initial date given.
    """
    # Formats the date string for manipulation
    date = date.replace("-", " ").replace("/", " ")
    date_list = date.split(" ")
    # Creating a list of months to find the month number and a 13th blank
        # element to catch none month entries
    months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", \
        "Oct", "Nov", "Dec", " "]
    # Finding the year, month, and day if the year is first in the date string
    if len(date_list[0]) == 4:
        year = date_list[0]
        month = date_list[1]
        day = date_list[2]
    # Finding the year, month, and day if the abbreviated month is first in
        # the date string using the months list
    elif len(date_list[0]) == 3:
        place = 1
        for month in months:
            if date_list[0] == month or place == 13:
                year = date_list[2]
                month = place
                day = date_list[1]
                break
            else:
                place += 1
    # Finding the year, month, and day if the numberical month is first in
        # the date string
    else:
        year = date_list[2]
        month = date_list[0]
        day = date_list[1]
    assert int(month) <= 12 and int(day) <= 31, "ERROR: Illegal date."
    return "{:d}-{:d}-{:d}".format(int(year), int(month), int(day))

main()
