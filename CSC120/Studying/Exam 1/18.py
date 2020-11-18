class LemonadeStand:

    def __init__(self):
        self._lemonade_dict = {}

    def add_lemonade(self, lemonade_string):
        if lemonade_string not in self._lemonade_dict:
            self._lemonade_dict[lemonade_string] = 0
        self._lemonade_dict[lemonade_string] += 1

    def get_amount(self, lemonade_string):
        return self._lemonade_dict[lemonade_string]

    def start_new_day(self):
        self._lemonade_dict = {}

    def __str__(self):
        string1 = ""
        for lemonade, amount in sorted(self._lemonade_dict.items()):
            string1 += lemonade + ": " + str(amount) + "\n"
        return string1

new_stand=LemonadeStand()
print(new_stand)
new_stand.add_lemonade("Red")
new_stand.add_lemonade("Blue")
new_stand.add_lemonade("Red")
print(new_stand)
print (new_stand.get_amount("Red"))
print(new_stand)
