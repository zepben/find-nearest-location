/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import com.zepben.annotations.EverythingIsNonnullByDefault;

import java.util.*;

import static java.util.stream.Collectors.toList;

@EverythingIsNonnullByDefault
@SuppressWarnings("WeakerAccess")
public class NearestLocationsMovablePoi<T> implements NearestLocations<T> {

    private final MissingLocationHandler missingLocationHandler;

    private LocationProvider<T> lcprPoi;
    private List<T> poiList;

    public NearestLocationsMovablePoi(Collection<T> poiCollection,
                                      LocationProvider<T> lcprPoi,
                                      MissingLocationHandler missingLocationHandler) {
        this.poiList = poiCollection.stream().distinct().collect(toList());
        this.lcprPoi = lcprPoi;
        this.missingLocationHandler = missingLocationHandler;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidLocationException if location value is not in given range
     */
    public <U> List<T> find(U entity, LocationProvider<U> lcprEntity, int n) {
        NavigableMap<Double, List<T>> distToPoisMap = new TreeMap<>();

        if (lcprEntity.hasLocation(entity)) {
            double entityLat = lcprEntity.lat(entity);
            double entityLon = lcprEntity.lon(entity);
            double entityEle = lcprEntity.ele(entity);

            if (!LocationUtility.validateLocation(entityLat, entityLon, entityEle)) {
                throw new InvalidLocationException();
            }

            for (T poi : poiList) {
                if (lcprPoi.hasLocation(poi)) {
                    double poiLat = lcprPoi.lat(poi);
                    double poiLon = lcprPoi.lon(poi);
                    double poiEle = lcprPoi.ele(poi);

                    if (!LocationUtility.validateLocation(poiLat, poiLon, poiEle)) {
                        throw new InvalidLocationException();
                    }

                    double squaredDistance = LocationUtility.calculateSquaredDistance(entityLat,
                        entityLon,
                        entityEle,
                        poiLat,
                        poiLon,
                        poiEle);

                    if (distToPoisMap.size() < n || squaredDistance <= distToPoisMap.lastKey()) {
                        distToPoisMap.computeIfAbsent(squaredDistance, k -> new ArrayList<>()).add(poi);
                        if (distToPoisMap.size() > n)
                            distToPoisMap.pollLastEntry();
                    }
                } else {
                    missingLocationHandler.handle(lcprPoi.id(poi), null);
                }
            }
            return distToPoisMap.values().stream().flatMap(Collection::stream).limit(n).collect(toList());
        } else {
            missingLocationHandler.handle(lcprEntity.id(entity), null);
            return Collections.emptyList();
        }
    }

}
