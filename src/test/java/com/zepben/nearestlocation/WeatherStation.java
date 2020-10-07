/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import javax.annotation.Nullable;

/**
 * Weather Station class for the purpose of testing
 * Will utilize network model in future for this purpose
 */

class WeatherStation {

    @Nullable LngLat lngLat;
    String id;
    double ele;

    WeatherStation(@Nullable LngLat lngLat, double ele, String id) {
        this.lngLat = lngLat;
        this.ele = ele;
        this.id = id;
    }

}
