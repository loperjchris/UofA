class Stack:
    def __init__(self):
        self._index = []
    def push(self, item):
        self._index.append(item)
    def pop(self):
        return self._index.pop()
    def peek(self):
        return self._index[-1]
    def length(self):
        return len(self._index)
    def is_empty(self):
        return self.length() == 0

def reverse_pairs(s):
    stack = Stack()
    ns = ""
    while s != "":
        stack.push(s[0])
        s = s[1:]
        if stack.length() == 2:
            while not stack.is_empty():
                ns += stack.pop()
    if not stack.is_empty():
        ns += stack.pop()
    return ns

print (reverse_pairs("test"))
print (reverse_pairs(""))
print (reverse_pairs("abc"))

"""def reverse_pairs(string):
    to_return = ""
    stack = Stack()
    while string != "":
        stack.push(string[0])
        string = string[1:]
        if len(stack) == 2:
            while not stack.is_empty():
                to_return += stack.pop()
    while not stack.is_empty():
        to_return += stack.pop()
    return to_return"""
