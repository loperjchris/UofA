class Node:

    def __init__(self, word):
        self._word = word
        self._count = 1
        self._next = None

    def word(self):
        return self._word

    def count(self):
        return self._count

    def next(self):
        return self._next

    def set_next(self, target):
        self._next = target

    def incr(self):
        self._count += 1

    def __str__(self):
        return self._word + " : " + self._count

class LinkedList:

    def __init__(self):
        self._head = None

    def is_empty(self):
        return self._head == None

    def head(self):
        return self._head

    def update_count(self, word):
        pass

    def rm_from_hd(self):
        pass

    def insert_after(self, node1, node2):
        pass

    def sort(self):
        pass

    def get_nth_highest_count(self, n):
        pass

    def print_upto_count(self, n):
        pass

    def __str__(self):
        pass

def main():
    pass

main()
