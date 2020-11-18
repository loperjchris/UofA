class Temp:
    def __init__(self, temp, unit):
        self._temp = temp
        self._unit = unit

    def __str__(self):
        return str(self._temp) + " " + self._unit

    def same_unit(self, other):
        if self._unit != other._unit:
            if self._unit == "C":
                self.convert()
            else:
                other.convert()

    def __eq__(self, other):
        self.same_unit(other)
        return self._temp == other._temp

    def __gt__(self, other):
        self.same_unit(other)
        return self._temp > other._temp

    def convert (self):
        print (self._temp)
        self._temp = (self._temp * (9.0/5.0)) + 32
        self._unit = "F"
        print (self._temp)

t1 = Temp(0, "C")
t2 = Temp(0, "F")
t3 = Temp(25, "C")
print (t1)
print (t1 > t2)
