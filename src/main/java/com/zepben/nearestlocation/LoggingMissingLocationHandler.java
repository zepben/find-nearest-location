/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import com.zepben.annotations.EverythingIsNonnullByDefault;
import org.slf4j.Logger;

import javax.annotation.Nullable;

@EverythingIsNonnullByDefault
@SuppressWarnings("WeakerAccess")
public class LoggingMissingLocationHandler implements MissingLocationHandler {

    private Logger logger;

    public LoggingMissingLocationHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(String id, @Nullable Throwable cause) {
        logger.warn(String.format("%s does not have location data", id));
    }

    Logger logger() {
        return logger;
    }

}
