# This class stores information about a location on Earth.  Locations are
# specified using latitude and longitude.  The class includes a method for
# computing the distance between two locations.

from math import *

RADIUS = 3963.1676  # Earth radius in miles

class GeoLocation:

    # constructs a geo location object with given latitude and longitude
    def __init__(self, latitude, longitude):
        self._latitude = float(latitude)
        self._longitude = float(longitude)

    # returns the latitude of this geo location
    def get_latitude(self):
        return self._latitude

    # returns the longitude of this geo location
    def get_longitude(self):
        return self._longitude

    # returns a string representation of this geo location
    def __str__(self):
        return "latitude: " + str(self._latitude) + ", longitude: " + str(self._longitude)

    # returns the distance in miles between this geo location and the given
    # other geo location
    def distance_from(self, other):
        lat1 = radians(self._latitude)
        long1 = radians(self._longitude)
        lat2 = radians(other._latitude)
        long2 = radians(other._longitude)
        # apply the spherical law of cosines with a triangle composed of the
        # two locations and the north pole
        the_cos = sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(long1 - long2)
        arc_length = acos(the_cos)
        return arc_length * RADIUS
