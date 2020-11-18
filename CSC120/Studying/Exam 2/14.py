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
    def __str__(self):
        curr = self._head
        s = ""
        while curr != None:
            s += str(curr._value)
            curr = curr._next
        return s
def keep_only_duplicates(alist):
    word_count = {}
    curr = alist._head
    while curr != None:
        if curr._value in word_count:
            word_count[curr._value] += 1
        else:
            word_count[curr._value] = 1
        curr = curr._next
    prev = None
    curr = alist._head
    while curr != None:
        if word_count[curr._value] == 1 and prev != None:
            prev._next = curr._next
            curr._next = None
            curr = prev._next
        elif word_count[curr._value] == 1 and prev == None:
            alist._head = curr._next
            curr._next = None
            curr = alist._head
        prev = curr
        curr = curr._next


ll = LinkedList()
ll.add(Node(1))
ll.add(Node(2))
ll.add(Node(3))
ll.add(Node(3))
ll.add(Node(1))
ll.add(Node("hello"))
ll.add(Node("world"))
ll.add(Node("hello"))
print (ll)
keep_only_duplicates(ll)
print (ll)
