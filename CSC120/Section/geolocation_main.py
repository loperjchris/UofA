from geolocation import *

def main():
    GS = GeoLocation(32.229776, -110.955038)
    London = GeoLocation(51.507351, -.0127758)
    print (GS.distance_from(London))

main()
