class Point:
    def __init__(self, x, y):
        self._x = x
        self._y = y

    def __eq__(self, other):
        return self._x == other._x and self._y == other._y

p1 = Point(1,2)
p2 = Point(3,4)
p3 = Point(1,2)

print (p1.__eq__(p2))
print (p1.__eq__(p3))
