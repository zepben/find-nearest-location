/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;


import com.zepben.testutils.junit.SystemLogExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class LocationUtilityTest {

    @Test
    public void coverage() {
        new LocationUtility();
    }

    @Test
    public void calculateDistance() {
        double distance = LocationUtility.calculateDistance(38.898556, -77.037852, 0, 38.897147, -77.043934, 0);
        assertThat(distance, closeTo(549, 1));  //expected accuracy of up to 1m for distance <1km

        distance = LocationUtility.calculateDistance(35.29943548054544, 149.14306685328484, 0, 33.85216970140739, 151.23046919703484, 0);
        assertThat(distance, closeTo(249800, 50));  //expected accuracy of up to 50m for distance(5-200)km

        distance = LocationUtility.calculateDistance(35.27536003090657, 149.1295158862522, 0, 35.248203361051694, 149.13393080245442, 0);
        assertThat(distance, closeTo(3050, 5)); //expected accuracy of up to 5m for distance(1-5)km
    }

    @Test
    public void testInvalidLocations() {
        assertThat(LocationUtility.validateLocation(-90.0001, 0, 0), is(false));
        assertThat(LocationUtility.validateLocation(90.0001, 0, 0), is(false));
        assertThat(LocationUtility.validateLocation(0, -180.0001, 0), is(false));
        assertThat(LocationUtility.validateLocation(0, 180.0001, 0), is(false));
        assertThat(LocationUtility.validateLocation(0, 0, -0.0001), is(false));
    }

}
