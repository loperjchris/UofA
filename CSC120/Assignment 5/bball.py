"""
    File: bball.py
    Author: Ruben Tequida
    Purpose: Compute and return the conference with the maximum average win
        ratio from a user given list of teams.
    Course: CSc 120, Section: 1H, Semester: Fall 2018
"""
from sys import *

class Team:

    def __init__(self, line):
        """
        Initializes a Team object and all of its attributes.

        Parameters: line is a string.

        Returns: None.

        Pre-condition: line isn't empty and doesn't begin with a # symbol.

        Post-condition: None.
        """
        assert line != "" and line[0][0] != "#"
        line = line.split("\t")
        # Testing to see if the first character in the line is a number.
        try:
            int(line[0])
            is_num = True
        except ValueError:
            is_num = False
        # If the first character is a number then it removes it from the line.
        if is_num:
            del line[0]
        place = 0
        # Removes any empty elements that may appear in the list line.
        for element in line:
            if element == "":
                del line[place]
            place += 1
        # Breaks up the first element of line to get the team name and the
            #conference.
        self._team_name = line[0].rsplit("(", 1)[0].strip(" ")
        self._conference = line[0].rsplit("(", 1)[1].strip(")")
        self._wins = float(line[1])
        self._losses = float(line[2])
        self._win_ratio = self._wins/(self._wins + self._losses)


    def name(self):
        return self._team_name

    def conf(self):
        return self._conference

    def win_ratio(self):
        return self._win_ratio

    def __str__(self):
        """
        Creates a string that's printed when print is called on a Team
            object.

        Parameters: None

        Returns: A string with the team name and team win ratio separated by
            a colon.
        """
        return "{} : {}".format(self._team_name, self._win_ratio)

class Conference:

    def __init__(self, conf):
        """
        Initializes a Conference object and creates an empty conf_teams
            list.

        Parameters: conf is a string.

        Returns: None.

        Pre-condition: conf is a string representing the teams conference.

        Post-condition: None.
        """
        assert isinstance(conf, str)
        self._conference = conf
        self._conf_teams = []

    def __contains__(self, team):
        """
        Checks if a Team object is within the list conf_teams.

        Parameters: team is a Team object.

        Returns: True or False.

        Pre-condition: team is a Team object.

        Post-condition: True or False.
        """
        ### INVARIANT: team is a Team class object.
        return team in self._conf_teams

    def name(self):
        return self._conference

    def add(self, team):
        """
        Adds a Team object to the list conf_teams.

        Parameters: team is a Team object.

        Returns: None.

        Pre-condition: team is a Team object.

        Post-condition: None.
        """
        ### INVARIANT: team is a Team class object.
        if team not in self._conf_teams:
            self._conf_teams.append(team)

    def win_ratio_avg(self):
        """
        Computes the average win ratio for a conference.

        Parameters: None

        Returns: The average win ratio for a conference.
        """
        win_ratio = 0
        # Adds all the win ratios of team in this conference which will be
            # used to compute the win ratio average.
        for team_obj in self._conf_teams:
            ### INVARIANT: team_obj is a Team class object and
                ### self._conf_teams is a list of Team class objects.
            win_ratio += team_obj._win_ratio
        return win_ratio/len(self._conf_teams)

    def __str__(self):
        """
        Creates a string that's printed when print is called on a
            Conference object.

        Parameters: None

        Returns: A string with the conference name and conference win ratio
            average separated by a colon.
        """
        return "{} : {}".format(self._conference, self.win_ratio_avg())

class ConferenceSet:

    def __init__(self):
        """
        Initializes a ConferenceSet object by creating an empty conferences
            set.

        Parameters: None.

        Returns: None.
        """
        self._conferences = set()

    def add(self, team):
        """
        Adds a team object to the set conferences.

        Parameters: team is a Team object.

        Returns: None.

        Pre-condition: team is a Team object.

        Post-condition: None.
        """
        ### INVARIANT: team is a Team class object.
        in_set = False
        # Checks to see if the team object is in the Conference list
            # conf_teams and if the conference object is part of the
            # ConferenceSet set and adds them if they're not.
        for conference in self._conferences:
            ### INVARIANT: conference is a Conference class object and
                ### self._conferences is a set of Conference class objects.
            if team in conference:
                in_set = True
        if in_set == False:
            self._conferences.add(Conference(team.conf()))
        for conference in self._conferences:
            ### INVARIANT: conference is a Conference class object and
                ### self._conferences is a set of Conference class objects.
            if team.conf() == conference.name():
                conference.add(team)

    def best(self):
        """
        Computes and prints the maximum average of win ratios across all
            conferences.

        Parameters: None.

        Returns: None.
        """
        max_avg = 0
        best_conferences = []
        # Finds the maximum of all the win ratio averages and creates a list
            # of conference that match that value.
        for conference in self._conferences:
            ### INVARIANT: conference is a Conference class object and
                ### self._conferences is a set of Conference class objects.
            max_avg = max(max_avg, conference.win_ratio_avg())
        for conference in self._conferences:
            ### INVARIANT: conference is a Conference class object and
                ### self._conferences is a set of Conference class objects.
            if conference.win_ratio_avg() == max_avg:
                best_conferences.append(conference)
        for conference in best_conferences:
            ### INVARIANT: conference is a Conference class object and
                ### best_conferences is a list of Conference class objects.
            print (conference)


def main():
    """
    Opens the desired file and feeds the lines into the Team class.

    Parameters: None.

    Returns: None.
    """
    filename = input()
    # Catching whether the file could be opened or not.
    try:
        file = open(filename)
    except:
        print ("ERROR: Could not open file " + filename)
        exit(1)
    data = file.read().split("\n")
    file.close()
    conference_set = ConferenceSet()
    # Skips lines that are empty or comment lines that begin with a # symbol.
    for line in data:
        if line != "":
            if line[0][0] != "#":
                team = Team(line)
                conference_set.add(team)
    conference_set.best()

main()
