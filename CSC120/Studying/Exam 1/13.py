def movieTimeLine():
    file = open("movies.txt")
    data = file.read().split("\n")
    file.close()
    newdict = {}
    for line in data:
        line = line.split(",")
        if line[1] not in newdict:
            newdict[line[1]] = {}
        newdict[line[1]][line[2]] = line[0]
    actorname = input()
    for year, movie in sorted(newdict[actorname].items()):
        print ("{}, {}".format(year, movie))

movieTimeLine()
