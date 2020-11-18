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

class Queue:
    def __init__(self):
        self._head = None
        self._tail = None
    def enqueue(self, item):
        curr = self._head
        if curr == None:
            self._head = item
            self._tail = item
        else:
            self._tail.set_next(item)
            self._tail = item
    def dequeue(self):
        if self._head != None:
            curr = self._head
            self._head = curr.get_next()
            curr.set_next(None)
            return curr
    def __str__(self):
        curr = self._head
        s = ""
        while curr != None:
            s += str(curr.get_value())
            curr = curr.get_next()
        return s

queue = Queue()
queue.enqueue(Node(1))
queue.enqueue(Node(2))
queue.enqueue(Node(3))
print (queue)
print (queue.dequeue())
print (queue)
