file = open("students.csv")
data = file.read().split("\n")
file.close()
dic = {}
for line in data:
    line = line.split(",")
    i = 0
    while i < len(line):
        if i == 0:
            if line[i] not in dic and i == 0:
                dic[line[i]] = {}
            i += 1
        else:
            if line[i] not in dic[line[0]]:
                dic[line[0]][line[i]] = line[i + 1]
                i += 2
            """elif line[i] in dic[line[0]]:
                dic[line[0]][line[i]] += line[i + 1]
                i += 2"""
print (dic)
