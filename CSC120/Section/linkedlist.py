class Node:
    def __init__(self,value):
        self._value = value
        self._next = None

    def __str__(self):
        nxt = self._next
        if nxt == None:
            nxt = "None"
        else:
            nxt = "-> "
        return "|" + str(self._value) + "|:" + nxt

class LinkedList:

    def __init__(self):
        self._head = None

    def adxd(self,new):
        new._next = self._head
        self._head = new

    def print_llist(self):
        current = self._head
        while current != None:
            print (current, end = "")
            current = current._next

alist = LinkedList()
print(alist)
n = Node("Hello")
print (str(n))
alist.add(n)
n = Node("world!")
print (str(n))
alist.add(n)
alist.print_llist()
