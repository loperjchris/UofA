import random
class Node:
    def __init__(self,value):
        self._value = value
        self._next = None
    def __str__(self):
        return str(self._value)
class LinkedList:
    def __init__(self):
        self._head = None
    def add_to_head(self,new):
        new._next = self._head
        self._head = new
    def remove_first(self):
        if self._head == None:
            return None
        else:
            n = self._head
            self._head = n._next
            n._next = None
            return n
    def add_to_end(self, new):
        if self._head == None:
            self._head = new
        else:
            current = self._head
            prev = current
            while current != None:
                prev = current
                current = current._next
            prev._next = new
    def random_list(self):
        new_ll = LinkedList()
        while self._head != None:
            n = random.randint(0, 1)
            to_add = self.remove_first()
            if n == 0:
                new_ll.add_to_head(to_add)
            else:
                new_ll.add_to_end(to_add)
        print (new_ll)
    def __str__(self):
        curr = self._head
        s = ""
        while curr != None:
            s += str(curr._value)
            curr = curr._next
        return s

ll = LinkedList()
ll.add_to_head(Node(1))
ll.add_to_head(Node(2))
ll.add_to_head(Node(3))
ll.add_to_head(Node(4))
ll.add_to_head(Node(5))
ll.add_to_head(Node(6))
print (ll)
ll.random_list()
