class Collection:
    def __init__(self):
        self._sabers = {}
    def add(self, owner, color):
        if color.lower() not in self._sabers:
            self._sabers[color.lower()] = [owner]
        else:
            self._sabers[color.lower()].append(owner)
        print ("this will make a fine addition to my collection")
    def colors_in_collection(self):
        for color in self._sabers:
            print ("{} {}".format(len(self._sabers[color]), color))
    def __str__(self):
        s = ""
        for color in self._sabers:
            for owner in self._sabers[color]:
                s += owner + "\n"
        return s
New_collection = Collection()
New_collection.add("Anakin Skywalker","blue")
New_collection.add("Obi-Wan Kenobi","Blue")
New_collection.colors_in_collection()
print(New_collection)
