class Studen:
    def __init__(self, name, id):
        self._name = name
        self._id = id

    def __eq__(self, other):
        return self._name == other._name and self._id == other._id

s1 = Student(John, )
