/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import com.zepben.testutils.exception.ExpectException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class LocationProviderBuilderTest {

    @Test
    public void build() {
        final LocationProvider<ThingWithLocation> locationLocationProvider = LocationProvider.<ThingWithLocation>builder()
            .latitudeProvider(s -> s.lat)
            .longitudeProvider(s -> s.lon)
            .elevationProvider(s -> s.ele)
            .idProvider(s -> s.id)
            .hasLocationProvider(s -> false)
            .build();

        final ThingWithLocation thingWithLocation = new ThingWithLocation(0.0, 1.0, 2.0, "id");

        assertEquals(0.0, locationLocationProvider.lon(thingWithLocation), 0.00001);
        assertEquals(1.0, locationLocationProvider.lat(thingWithLocation), 0.00001);
        assertEquals(2.0, locationLocationProvider.ele(thingWithLocation), 0.00001);
        assertEquals("id", locationLocationProvider.id(thingWithLocation));
        assertFalse(locationLocationProvider.hasLocation(thingWithLocation));
    }

    @Test
    public void defaultValues() {
        final LocationProvider<ThingWithLocation> locationLocationProvider = LocationProvider.<ThingWithLocation>builder()
            .latitudeProvider(s -> s.lat)
            .longitudeProvider(s -> s.lon)
            .build();

        final ThingWithLocation thingWithLocation = new ThingWithLocation(0.0, 1.0, 2.0, "id");

        assertEquals(0.0, locationLocationProvider.lon(thingWithLocation), 0.00001);
        assertEquals(1.0, locationLocationProvider.lat(thingWithLocation), 0.00001);
        assertEquals(0.0, locationLocationProvider.ele(thingWithLocation), 0.00001);
        assertEquals("", locationLocationProvider.id(thingWithLocation));
        assertTrue(locationLocationProvider.hasLocation(thingWithLocation));
    }

    @Test
    public void doesNotBuildWithoutLonOrLats() {
        final LocationProvider.Builder<ThingWithLocation> builderWithoutLon = LocationProvider.<ThingWithLocation>builder().latitudeProvider(s -> s.lat);
        final LocationProvider.Builder<ThingWithLocation> builderWithoutLat = LocationProvider.<ThingWithLocation>builder().longitudeProvider(s -> s.lon);
        ExpectException.expect(builderWithoutLat::build).toThrow(IllegalStateException.class);
        ExpectException.expect(builderWithoutLon::build).toThrow(IllegalStateException.class);
    }

    @Test
    public void builderReuse() {
        final LocationProvider.Builder<Object> builder = LocationProvider.builder()
            .latitudeProvider(s -> 1)
            .longitudeProvider(s -> 2)
            .elevationProvider(s -> 3)
            .idProvider(s -> "id")
            .hasLocationProvider(s -> true);

        Object entity = new Object();
        LocationProvider<Object> lp = builder.build();
        assertThat(lp.lat(entity), is(1.));
        assertThat(lp.lon(entity), is(2.));
        assertThat(lp.ele(entity), is(3.));
        assertThat(lp.id(entity), is("id"));
        assertThat(lp.hasLocation(entity), is(true));

        builder.latitudeProvider(s -> -1)
            .longitudeProvider(s -> -2)
            .elevationProvider(s -> -3)
            .idProvider(s -> "nope")
            .hasLocationProvider(s -> false);

        assertThat(lp.lat(entity), is(1.));
        assertThat(lp.lon(entity), is(2.));
        assertThat(lp.ele(entity), is(3.));
        assertThat(lp.id(entity), is("id"));
        assertThat(lp.hasLocation(entity), is(true));
    }

    private class ThingWithLocation {

        double lon;
        double lat;
        double ele;
        String id;

        private ThingWithLocation(double lon, double lat, double ele, String id) {
            this.lon = lon;
            this.lat = lat;
            this.ele = ele;
            this.id = id;
        }

    }

}
