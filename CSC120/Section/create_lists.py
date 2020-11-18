from timeit import *

def test1():
    alist = []
    for i in range(1000):
        alist = alist + [i]

def test2():
    alist = []
    for i in range(1000):
        alist.append(i)

def test3():
    alist = list(range(1000))

test_1 = Timer("test1()", "from __main__ import test1")
test_2 = Timer("test2()", "from __main__ import test2")
test_3 = Timer("test3()", "from __main__ import test3")

print (test_1.timeit(number = 1000))
print (test_2.timeit(number = 1000))
print (test_3.timeit(number = 1000))
