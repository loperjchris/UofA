class Node:

    def __init__(self, value):
        self._value = value
        self._next = None

    def get_next(self):
        return self._next

    def __str__(self):
        return str(self._value)

class LinkedList:

    def __init__(self):
        self._head = None

    def get_head(self):
        return self._head

    def add(self,new):
        new._next = self._head
        self._head = new

def get_last(n):
    if n.get_next() == None:
        return n
    return get_last(n.get_next())

L = LinkedList()
L.add(Node(3))
L.add(Node(1))
L.add(Node(2))
node = L.get_head()
print (get_last(node))
