def is_prime(n):
    if n == 2 or n == 3:
        return True
    elif n % 2 == 0:
        return False
    return helper(n, 3)

def helper(n, i):
    if i >= int(n**0.5):
        return True
    elif n % i == 0:
        return False
    return helper(n, i + 2)

print (is_prime(15))



"""def is_prime(n):
    return helper(n, 2)
def helper(n, i):
    if n == i:
        return True
    elif n % i == 0:
        return False
    else:
        return helper(n, i+1)"""
