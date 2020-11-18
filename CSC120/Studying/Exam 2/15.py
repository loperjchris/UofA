class Node:
    def __init__(self,value):
        self._value = value
        self._next = None
    def get_value(self):
        return self._value
    def get_next(self):
        return self._next
    def set_value(self, value):
        self._value = value
    def set_next(self, next):
        self._next = next
    def __str__(self):
        return str(self._value)
class Stack:
    def __init__(self):
        self._head = None
    def push(self, item):
        curr = self._head
        self._head = item
        item._next = curr
    def pop(self):
        if self._head != None:
            curr = self._head
            self._head = curr._next
            curr._next = None
            return curr
    def __str__(self):
        curr = self._head
        s = ""
        while curr != None:
            s += str(curr.get_value())
            curr = curr._next
        return s

stack = Stack()
stack.push(Node(1))
stack.push(Node(2))
stack.push(Node(3))
print (stack)
print (stack.pop())
print (stack)
