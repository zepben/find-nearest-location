/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

class LngLat {

    private final double longitude;
    private final double latitude;

    LngLat(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    double longitude() {
        return this.longitude;
    }

    double latitude() {
        return this.latitude;
    }

}
