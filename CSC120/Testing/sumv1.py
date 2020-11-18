import time

"""
sumv1 - computes the sum of the first n integers using a loop
"""
def sumv1(n):
    start = time.time()

    num = 0
    for i in range(1,n+1):
        num += i

    end = time.time()

    taken = end - start

    return "sum = %d : running time required was %10.7f seconds"%(num, taken)

print (sumv1(1000000))
