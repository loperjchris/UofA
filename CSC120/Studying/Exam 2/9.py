def recursive_primes(n):
    if n == 2:
        print (2)
    elif n == 3:
        print (2)
        print (3)
    else:
        if n % 2 == 0:
            n -= 1
        recursive_primes(n - 2)
        if is_prime(n):
            print (n)

def is_prime(n):
    if n == 2 or n == 3:
        return True
    return helper(n, 3)

def helper(n, i):
    sr = int(n**0.5)
    if i >= sr:
        return True
    elif n % i == 0:
        return False
    return helper(n, i + 2)

recursive_primes(20)

"""Solution:
def recursive_primes(n):
    if n == 2:
        print(2)
    else:
        recursive_primes(n-1)
        if is_prime(n):
            print(n)"""
