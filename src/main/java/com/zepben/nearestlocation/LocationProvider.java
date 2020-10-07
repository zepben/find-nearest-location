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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * Interface consists of functions for accessing entity location data
 *
 * @param <T> Entity type
 */
@EverythingIsNonnullByDefault
public interface LocationProvider<T> {

    /**
     * Returns longitude data for the entity
     *
     * @param entity an object that holds location information
     * @return lon in degrees
     */
    double lon(T entity);

    /**
     * Returns latitude data for the entity
     *
     * @param entity an object that holds location information
     * @return lat in degrees
     */
    double lat(T entity);

    /**
     * Returns elevation data for the entity
     *
     * @param entity an object that holds location information
     * @return elevation in metres
     */
    double ele(T entity);

    /**
     * Returns id for the entity
     *
     * @param entity an object that holds location information
     * @return string entity id
     */
    String id(T entity);

    /**
     * Checks if entity has location data
     *
     * @param entity an object that holds location information
     * @return true if entity has location data
     */
    boolean hasLocation(T entity);

    /**
     * Method that returns a builder that allows you to build a LocationProvider instance based on lambdas / method references.
     * <p>
     * Using this to "implement" a location provider for your entity type will be less lines of code than a declared
     * class that implements the interface.
     *
     * @param <T> The entity type that provides a location
     * @return a LocationProvider builder
     */
    static <T> LocationProvider.Builder<T> builder() {
        return new LocationProvider.Builder<>();
    }

    @EverythingIsNonnullByDefault
    @SuppressWarnings("WeakerAccess")
    class Builder<T> {

        private @Nullable ToDoubleFunction<T> latitudeProvider;
        private @Nullable ToDoubleFunction<T> longitudeProvider;
        private ToDoubleFunction<T> elevationProvider = t -> 0.0;
        private Function<T, String> idProvider = t -> "";
        private Predicate<T> hasLocation = t -> true;

        private Builder() {
        }

        public LocationProvider<T> build() {
            String msg = "";
            if (latitudeProvider == null)
                msg += "Latitude provider missing. ";

            if (longitudeProvider == null)
                msg += "Longitude provider missing. ";

            if (!msg.isEmpty())
                throw new IllegalStateException("Could not create LocationProvider instance due to missing arguments. " + msg);

            return new LocationProvider<T>() {
                private ToDoubleFunction<T> getLat = latitudeProvider;
                private ToDoubleFunction<T> getLon = longitudeProvider;
                private ToDoubleFunction<T> getElevation = elevationProvider;
                private Function<T, String> getId = idProvider;
                private Predicate<T> getHasLocation = hasLocation;

                @Override
                public double lon(T entity) {
                    return getLon.applyAsDouble(entity);
                }

                @Override
                public double lat(T entity) {
                    return getLat.applyAsDouble(entity);
                }

                @Override
                public double ele(T entity) {
                    return getElevation.applyAsDouble(entity);
                }

                @Override
                public String id(T entity) {
                    return getId.apply(entity);
                }

                @Override
                public boolean hasLocation(T entity) {
                    return getHasLocation.test(entity);
                }
            };
        }

        public Builder<T> latitudeProvider(ToDoubleFunction<T> latitudeProvider) {
            this.latitudeProvider = latitudeProvider;
            return this;
        }

        public Builder<T> longitudeProvider(ToDoubleFunction<T> longitudeProvider) {
            this.longitudeProvider = longitudeProvider;
            return this;
        }

        public Builder<T> elevationProvider(ToDoubleFunction<T> elevationProvider) {
            this.elevationProvider = elevationProvider;
            return this;
        }

        public Builder<T> idProvider(Function<T, String> idProvider) {
            this.idProvider = idProvider;
            return this;
        }

        public Builder<T> hasLocationProvider(Predicate<T> hasLocationProvider) {
            this.hasLocation = hasLocationProvider;
            return this;
        }

    }

}
