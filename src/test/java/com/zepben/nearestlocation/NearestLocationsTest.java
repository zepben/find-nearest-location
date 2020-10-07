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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zepben.testutils.exception.ExpectException.expect;
import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NearestLocationsTest {


    @RegisterExtension
    public final SystemLogExtension systemErrRule = SystemLogExtension.SYSTEM_ERR.captureLog().muteOnSuccess();

    private static final Logger logger = LoggerFactory.getLogger(NearestLocationsTest.class);
    private final MissingLocationHandler missingLocationHandler = new LoggingMissingLocationHandler(logger);

    private final List<WeatherStation> poiList = new ArrayList<>();

    @SuppressWarnings("ConstantConditions")
    private final LocationProvider<WeatherStation> lcprWs = LocationProvider.<WeatherStation>builder()
        .latitudeProvider(s -> s.lngLat.latitude())
        .longitudeProvider(s -> s.lngLat.longitude())
        .elevationProvider(s -> s.ele)
        .idProvider(s -> s.id)
        .hasLocationProvider(s -> nonNull(s.lngLat))
        .build();

    @SuppressWarnings("ConstantConditions")
    private final LocationProvider<Transformer> lcprTr = LocationProvider.<Transformer>builder()
        .latitudeProvider(t -> t.lngLat.latitude())
        .longitudeProvider(t -> t.lngLat.longitude())
        .elevationProvider(t -> t.ele)
        .idProvider(t -> t.id)
        .hasLocationProvider(t -> nonNull(t.lngLat))
        .build();

    @Test
    public void findNearestLocations() {
        poiList.clear();
        LngLat lngLat = new LngLat(152.19558816, -33.62731914);
        WeatherStation w1 = new WeatherStation(lngLat, 0, "w1");
        lngLat = new LngLat(144.2834462, -34.63053747);
        WeatherStation w2 = new WeatherStation(lngLat, 0, "w2");
        lngLat = new LngLat(149.13, -35.28);
        WeatherStation w3 = new WeatherStation(lngLat, 0, "w3");
        lngLat = new LngLat(149.13, -35.28);
        WeatherStation w4 = new WeatherStation(lngLat, 0, "w4");
        poiList.add(w1);
        poiList.add(w2);
        poiList.add(w3);
        poiList.add(w4);

        lngLat = new LngLat(149.13000920000002, -35.2809368);
        Transformer t1 = new Transformer(lngLat, 0, "tr1");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        List<WeatherStation> closestPoi = entity.find(t1, lcprTr, 4);
        assertThat(closestPoi, contains(w3, w4, w1, w2));
    }

    @Test
    public void findNearestLocationsReturnsNResults() {
        poiList.clear();
        LngLat lngLat = new LngLat(152.19558816, -33.62731914);
        WeatherStation w1 = new WeatherStation(lngLat, 0, "w1");
        WeatherStation w2 = new WeatherStation(lngLat, 0, "w2");
        WeatherStation w3 = new WeatherStation(lngLat, 0, "w3");
        WeatherStation w4 = new WeatherStation(lngLat, 0, "w4");
        poiList.add(w1);
        poiList.add(w2);
        poiList.add(w3);
        poiList.add(w4);

        lngLat = new LngLat(149.13000920000002, -35.2809368);
        Transformer t1 = new Transformer(lngLat, 0, "tr1");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        List<WeatherStation> closestPoi = entity.find(t1, lcprTr, 2);
        assertThat(closestPoi, contains(w1, w2));
    }

    @Test
    public void findNearestLocationsReturnsNResultsInOrder() {
        poiList.clear();
        WeatherStation w1 = new WeatherStation(new LngLat(1, 1), 0, "w1");
        WeatherStation w2 = new WeatherStation(new LngLat(1, 2), 0, "w2");
        WeatherStation w3 = new WeatherStation(new LngLat(1, 3), 0, "w3");
        WeatherStation w4 = new WeatherStation(new LngLat(1, 4), 0, "w4");
        poiList.add(w1);
        poiList.add(w2);
        poiList.add(w3);
        poiList.add(w4);

        Transformer t1 = new Transformer(new LngLat(1, 0), 0, "tr1");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        List<WeatherStation> closestPoi = entity.find(t1, lcprTr, 2);
        assertThat(closestPoi, contains(w1, w2));

        Collections.reverse(poiList);

        entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        closestPoi = entity.find(t1, lcprTr, 2);
        assertThat(closestPoi, contains(w1, w2));
    }

    @Test
    public void testIncorrectLatPoi() {
        poiList.clear();
        LngLat lngLat = new LngLat(13, -190);
        WeatherStation w = new WeatherStation(lngLat, 10, "w3");
        poiList.add(w);
        lngLat = new LngLat(149.13000920000002, -35.2809368);
        Transformer t = new Transformer(lngLat, 0, "tr");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        expect(() -> {
            entity.find(t, lcprTr);
        }).toThrow(InvalidLocationException.class);
    }

    @Test
    public void testIncorrectLonPoi() {
        poiList.clear();
        LngLat lngLat = new LngLat(181, -180);
        WeatherStation w = new WeatherStation(lngLat, 10, "w3");
        poiList.add(w);
        lngLat = new LngLat(149.13000920000002, -35.2809368);
        Transformer t = new Transformer(lngLat, 0, "tr");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        expect(() -> {
            entity.find(t, lcprTr);
        }).toThrow(InvalidLocationException.class);
    }

    @Test
    public void testIncorrectElePoi() {
        poiList.clear();
        LngLat lngLat = new LngLat(180, -180);
        WeatherStation w = new WeatherStation(lngLat, -1, "w3");
        poiList.add(w);
        lngLat = new LngLat(149.13000920000002, -35.2809368);
        Transformer t = new Transformer(lngLat, 0, "tr");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        expect(() -> {
            entity.find(t, lcprTr);
        }).toThrow(InvalidLocationException.class);
    }

    @Test
    public void testIncorrectLatEntity() {
        poiList.clear();
        LngLat lngLat = new LngLat(179, 180);
        WeatherStation w = new WeatherStation(lngLat, 10, "w3");
        poiList.add(w);
        lngLat = new LngLat(149.13000920000002, -181.2809368);
        Transformer t = new Transformer(lngLat, 0, "tr");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        expect(() -> {
            entity.find(t, lcprTr);
        }).toThrow(InvalidLocationException.class);
    }

    @Test
    public void testIncorrectLonEntity() {
        poiList.clear();
        LngLat lngLat = new LngLat(179, 180);
        WeatherStation w = new WeatherStation(lngLat, 10, "w3");
        poiList.add(w);
        lngLat = new LngLat(180.13000920000002, -180.00);
        Transformer t = new Transformer(lngLat, 0, "tr");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        expect(() -> {
            entity.find(t, lcprTr);
        }).toThrow(InvalidLocationException.class);
    }

    @Test
    public void testIncorrectEleEntity() {
        poiList.clear();
        LngLat lngLat = new LngLat(179, 180);
        WeatherStation w = new WeatherStation(lngLat, 10, "w3");
        poiList.add(w);
        lngLat = new LngLat(149.13000920000002, -179.2809368);
        Transformer t = new Transformer(lngLat, -0.1, "tr");

        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        expect(() -> {
            entity.find(t, lcprTr);
        }).toThrow(InvalidLocationException.class);
    }

    @Test
    public void testNullPoiLocation() {
        poiList.clear();
        LngLat lngLat = null;
        @SuppressWarnings("ConstantConditions") WeatherStation w1 = new WeatherStation(lngLat, 10, "w1");
        poiList.add(w1);
        lngLat = new LngLat(140, -34);
        WeatherStation w2 = new WeatherStation(lngLat, 10, "w2");
        poiList.add(w2);
        lngLat = new LngLat(149.13000920000002, -35.2809368);
        Transformer t = new Transformer(lngLat, 0, "tr1");
        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        List<WeatherStation> closestPoi = entity.find(t, lcprTr, 1);
        assertThat(closestPoi, contains(w2));

        String consoleOutput = systemErrRule.getLog();
        assertThat(consoleOutput, containsString("w1 does not have location data"));

        assertThat(entity.find(t, lcprTr), equalTo(w2));
    }

    @Test
    public void testNullEntityLocation() {
        poiList.clear();
        LngLat lngLat = new LngLat(149.13000920000002, -35.2809368);
        WeatherStation w = new WeatherStation(lngLat, 10, "w1");
        poiList.add(w);
        lngLat = null;
        @SuppressWarnings("ConstantConditions") Transformer t = new Transformer(lngLat, 0, "tr1");
        NearestLocations<WeatherStation> entity = new NearestLocationsMovablePoi<>(poiList, lcprWs, missingLocationHandler);
        List<WeatherStation> closestPoi = entity.find(t, lcprTr, 1);
        assertTrue(closestPoi.isEmpty());

        String consoleOutput = systemErrRule.getLog();
        assertThat(consoleOutput, containsString("tr1 does not have location data"));

        assertThat(entity.find(t, lcprTr), nullValue());
    }

}

