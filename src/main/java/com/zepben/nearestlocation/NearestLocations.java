/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import com.zepben.annotations.EverythingIsNonnullByDefault;

import javax.annotation.Nullable;
import java.util.List;

@EverythingIsNonnullByDefault
public interface NearestLocations<T> {

    /**
     * Finds list of nearest POIs for an entity
     *
     * @param entity     Entity for which POIs have to be found
     * @param lcprEntity LocationProvider for entity
     * @return The nearest POI
     */
    @Nullable
    default <U> T find(U entity, LocationProvider<U> lcprEntity) {
        List<T> results = find(entity, lcprEntity, 1);
        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    /**
     * Finds list of 'n' number of nearest POIs for an entity
     *
     * @param entity     Entity for which POIs have to be found
     * @param lcprEntity LocationProvider for entity
     * @param n          number of POI to be found
     * @return List of nearest POIs, ordered from closest to furthest
     */
    <U> List<T> find(U entity, LocationProvider<U> lcprEntity, int n);

}
