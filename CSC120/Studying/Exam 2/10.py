def is_in_string(s, c):
    if s == "":
        return False
    elif s[0] == c:
        return True
    return is_in_string(s[1:], c)

print (is_in_string("hello", "a"))



"""def is_in_string(s, c):
    if s == ‘’:
        return False
    if s[0] == c:
        return True
    else:
        return is_in_string(s[1:], c)"""
