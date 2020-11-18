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
def r_sum(curr):
    if curr == None:
        return 0
    return curr._value + r_sum(curr.get_next())

LL = LinkedList()
LL.add(Node(7))
LL.add(Node(6))
LL.add(Node(5))
print(r_sum(LL._head))

"""def r_sum(curr):
    if curr == None:
        return 0
    return curr._value + r_sum(curr._next)"""
