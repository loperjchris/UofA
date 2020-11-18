class Counter:
    def __init__(self, name):
        self._name = name
        self._count = 0

    def click(self):
        self._count += 1

    def __repr__(self):
        return "Counter('" + self._name + "')"

    def __str__(self):
        return self._name + ", " + str(self._count)

c = Counter("hello")
c.click()
c.click()
c.click()
f = c.__repr__()
print (f)
print (c)
