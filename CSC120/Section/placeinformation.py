from geolocation import *

class PlaceInformation:

    def __init__(self, name, address, tag, latitude, longitude):
        self._name = name
        self._address = address
        self._tag = tag
        self._geolocation = GeoLocation(latitude, longitude)

    def __str__(self):
        return self._name

    def get_name(self):
        return self._name + self._address + self._tag

    def get_address(self):
        return self._address

    def get_tag(self):
        return self._tag

    def distance_from(self, other):
        return self._geolocation.distance_from(other)
