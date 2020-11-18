import time

"""
sumv2 - computes the sum of the first n integers using a closed equation

"""
def sumv2(n):
    start = time.time()

    num = (n*(n+1))/2

    end = time.time()

    taken = end - start

    return "sum = %d : running time required was %10.7f seconds"%(num, taken)

print (sumv2(1000))
