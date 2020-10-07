/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import com.zepben.annotations.EverythingIsNonnullByDefault;

/**
 * InvalidLocationException for invalid location values
 */
@EverythingIsNonnullByDefault
@SuppressWarnings("WeakerAccess")
public class InvalidLocationException extends RuntimeException {

    public InvalidLocationException() {
        super();
    }

    public InvalidLocationException(String message) {
        super(message);
    }

}
