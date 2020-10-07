/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import com.zepben.annotations.EverythingIsNonnullByDefault;

@EverythingIsNonnullByDefault
@SuppressWarnings("WeakerAccess")
public final class LocationUtility {

    /**
     * Calculates distance between two locations
     * considering the elevation difference between them
     * Distance is calculated using the Haversine method
     *
     * @param lat1 Latitude info for location 1
     * @param lon1 Longitude info for location 1
     * @param ele1 Elevation info for location 1
     * @param lat2 Latitude info for location 2
     * @param lon2 Longitude info for location 2
     * @param ele2 Elevation info for location 2
     * @return Distance in Meters
     * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html">Haversine Distance Calculation</a>
     */
    public static double calculateDistance(double lat1,
                                           double lon1,
                                           double ele1,
                                           double lat2,
                                           double lon2,
                                           double ele2) {
        return Math.sqrt(calculateSquaredDistance(lat1, lon1, ele1, lat2, lon2, ele2));
    }

    /**
     * Calculates squared distance between two locations
     * Can be used when distance comparison needs to be done
     * Distance is calculated using the Haversine method
     *
     * @param lat1 Latitude info for location 1
     * @param lon1 Longitude info for location 2
     * @param ele1 Elevation info for location 2
     * @param lat2 Latitude info for location 2
     * @param lon2 Longitude info for location 2
     * @param ele2 Elevation info for location 2
     * @return Distance in Meters
     * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html">Haversine Distance Calculation</a>
     */
    public static double calculateSquaredDistance(double lat1,
                                                  double lon1,
                                                  double ele1,
                                                  double lat2,
                                                  double lon2,
                                                  double ele2) {
        final double R = 6371008; //Earth's radius in metres
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double sinLatDistance = Math.sin(latDistance / 2);
        double sinLonDistance = Math.sin(lonDistance / 2);
        double a = (sinLatDistance * sinLatDistance)
            + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * (sinLonDistance * sinLonDistance));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // converting distance into metres
        double height = ele1 - ele2;
        distance = (distance * distance) + (height * height);

        return distance;
    }

    /**
     * Does location validation by checking if latitude,longitude,elevation values fall within
     * the acceptable range
     *
     * @param lat Latitude info for the location
     * @param lon Longitude info for the location
     * @param ele Elevation info for the location
     * @return boolean whether or not the location is valid
     */
    public static boolean validateLocation(double lat, double lon, double ele) {
        return ((lat >= -90 && lat <= 90) && (lon >= -180 && lon <= 180) && (ele >= 0));
    }

}
