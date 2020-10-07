/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.nearestlocation;

import org.junit.jupiter.api.Test;

import static com.zepben.testutils.exception.ExpectException.expect;

public class InvalidLocationExceptionTest {

    @Test
    public void coverage() {
        expect(() -> {
            throw new InvalidLocationException();
        }).toThrow(InvalidLocationException.class);
        expect(() -> {
            throw new InvalidLocationException("test");
        }).toThrow(InvalidLocationException.class);
    }

}
