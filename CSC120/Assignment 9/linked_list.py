class Node:

    def __init__(self, name):
        """
        Initializes a Node class object and all of its attributes.

        Parameters: name is a string.

        Returns: None.

        Pre-condition: name is a string from a given text file.

        Post-condition: None.
        """
        self._name = name
        self._friends = None
        self._next = None

    def set_next(self, target):
        """
        Sets the ._next attribute to point to target.

        Parameters: target is a Node object.

        Returns: None.

        Pre-condition: target must be a completely initialized Node object.

        Post-condition: None.
        """
        self._next = target

    def set_friends(self, target):
        """
        Sets the ._friends attribute to point to target.

        Parameters: target is a Node object.

        Returns: None.

        Pre-condition: target must be a completely initialized Node object.

        Post-condition: None.
        """
        self._friends = target

    def name(self):
        """
        Getter method for name attribute.
        """
        return self._name

    def friends(self):
        """
        Getter method for friends attribute.
        """
        return self._friends

    def next(self):
        """
        Getter method for next attribute.
        """
        return self._next

    def __str__(self):
        """
        Creates a string representation of a Node object.

        Parameters: None.

        Returns: A string with the name, friends, and next attributes.
        """
        return "Name: " + self._name + ", Friends: " + str(self._friends) + ", Next: " + str(self._next)

class LinkedList:

    def __init__(self):
        """
        Initializes a LinkedList class object.

        Parameters: None.

        Returns: None.
        """
        self._head = None

    def add_name(self, node):
        """
        Adds a name to the linked list if it isn't already in it.

        Parameters: node is a Node object.

        Returns: None.

        Pre-condition: node must be completely initialized Node object with a
            ._name attribute.

        Post-condition: None.
        """
        # Doesn't had the node if there already is a node with the same name.
        if not self.in_list(node.name()):
            node.set_next(self.head())
            self._head = node

    def add_friends(self, node1, node2):
        """
        Adds node2 to the friends linked list of node1.

        Parameters: node1 and node2 are Node objects.

        Returns: None.

        Pre-condition: node1 and node2 must be completely initialized Node
            objects with ._name and ._friends attributes.

        Post-condition: None.
        """
        # Creates a new node from node2 as to not disrupt the pointer to node2
            # in the main linked list.
        new_node = Node(node2.name())
        current = self.head()
        # Loops through the linked list to find the node that has the same
            # name as node1.
        while current != None:
            if current.name() == node1.name():
                break
            current = current.next()
        # Adds node2 to the beginning of the friends linked list of node1.
        new_node.set_next(current.friends())
        current.set_friends(new_node)

    def in_list(self, name):
        """
        Determines if the given name is already in the linked list.

        Parameters: name is a string.

        Returns: True or False.

        Pre-condition: name must be a string.

        Post-condition: Must be True or False.
        """
        current = self.head()
        # Iterates through the linked list and returns True if it finds the
            # name, returns False otherwise.
        while current != None:
            if current.name() == name:
                return True
            current = current.next()
        return False

    def find_mutual_friends(self, name1, name2):
        """
        Finds the ._friends attribute of the given names and prints out the
            friends they share in common.

        Parameters: name1 and name2 are a string.

        Returns: None.

        Pre-condition: name1 and name2 are strings of a name that must already
            be in the linked list.

        Post-condition: None.
        """
        friend1 = self.find_node(name1)
        friend2 = self.find_node(name2)
        first_found = False
        # Iterates through the ._friends linked list of friend1 and compares
            # those names to the names in the linked list of friend2.
        while friend1 != None:
            while friend2 != None:
                if friend1.name() == friend2.name():
                    # Prints Friends in common: only if mutual friends are
                        # found and only in the beginning.
                    if not first_found:
                        first_found = True
                        print ("Friends in common:")
                    print (friend1.name())
                friend2 = friend2.next()
            friend1 = friend1.next()
            friend2 = self.find_node(name2)

    def find_node(self, name):
        """
        Finds the node in the linked list that matches the name with the
            ._name attribute.

        Parameters: name is a string.

        Returns: current.friends() is a Node object.

        Pre-condition: name is a strings of a name that must already
            be in the linked list.

        Post-condition: current.friends() must be the first node of the
            ._friends attribute.
        """
        current = self.head()
        # Iterates through the linked list to find the node with the matching
            # name.
        while current != None:
            if current.name() == name:
                break
            current = current.next()
        return current.friends()

    def head(self):
        """
        Getter method for head attribute.
        """
        return self._head

    def __str__(self):
        """
        Creates a string representation of a LinkedList object.

        Parameters: None.

        Returns: A string starting with Head and pointing to each node in the
            linked list and ending with None.
        """
        ll_string = "Head ->\n"
        current = self.head()
        while current != None:
            ll_string += str(current) + " ->\n"
            current = current.next()
        ll_string += "None"
        return ll_string
