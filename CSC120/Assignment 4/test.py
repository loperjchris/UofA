def move_to_end(phrase, char):
    new_string = ""
    end_string = ""
    phrase = phrase.lower()
    char = char.lower()
    for element in phrase:
        if element in char:
            end_string += element
        else:
            new_string += element
    new_string += end_string
    print (new_string.upper())

move_to_end("Hello, World!", "!heLLO")
