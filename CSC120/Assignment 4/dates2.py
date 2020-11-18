class Date:
    def __init__(self, date, event):
        self._date = date
        self._event = [event]
    def add_event(self, event):
        self._event.append(event)
    def __str__(self):
        return str(self._date) + " : " + str(self._event)

class DateSet:
    def __init__(self):
        self._dates = {}
    def add_date(self, date):
        if date._date not in self._dates:
            self._dates[date._date] = date._event
        else:
            self._dates[date._date] += date._event
    def __str__(self):
        return str(self._dates)
    def get_events(self, date):
        if date in self._dates:
            return self._dates[date]


def main():
    get_date()

def get_date():
    file = open("in03.txt")
    data = file.read().split("\n")
    file.close()
    dates = DateSet()
    for line in data:
        line = line.split(":")
        assert line[0][0] == "I" or line[0][0] == "R", "ERROR: Illegal operation."
        date_line = line[0].strip("I ").strip("R ")
        date = format_date(date_line)
        if line[0][0] == "I":
            event = line[1].strip(" ")
            d1 = Date(date, event)
            dates.add_date(d1)
        elif line[0][0] == "R":
            events = dates.get_events(date)
            events.sort()
            for element in events:
                print ("{}: {}".format(str(date), element))

def format_date(date):
    date = date.replace("-", " ").replace("/", " ")
    date_list = date.split(" ")
    months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
    if len(date_list[0]) == 4:
        year = date_list[0]
        month = date_list[1]
        day = date_list[2]
    elif len(date_list[0]) == 3:
        place = 1
        for month in months:
            if date_list[0] == month:
                year = date_list[2]
                month = place
                day = date_list[1]
                break
            else:
                place += 1
    else:
        year = date_list[2]
        month = date_list[0]
        day = date_list[1]
    assert int(month) <= 12 and int(day) <= 31, "ERROR: Illegal date."
    return "{:d}-{:d}-{:d}".format(int(year), int(month), int(day))

main()
