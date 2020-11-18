class Car:
    def __init__(self, make, model, speed):
        self._make = make
        self._model = model
        self._speed = speed
    def __str__(self):
        return self._make + " " + self._model + " has the top speed of " \
            + str(self._speed) + " mph"
    def shortest_time(self, time):
        return time/self._speed
dreamcar = Car("Lamborghini","Aventador",216)#216 in miles per hour
print(dreamcar)
print (dreamcar.shortest_time(432))#in miles
