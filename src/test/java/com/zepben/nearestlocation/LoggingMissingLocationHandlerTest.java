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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class LoggingMissingLocationHandlerTest {


    @RegisterExtension
    public final SystemLogExtension systemErrRule = SystemLogExtension.SYSTEM_ERR.captureLog().muteOnSuccess();

    @Test
    public void coverage() {
        Logger logger = mock(Logger.class);
        LoggingMissingLocationHandler handler = new LoggingMissingLocationHandler(logger);
        assertThat(handler.logger(), equalTo(logger));

        handler.handle("MyTestId", mock(Throwable.class));

        verify(logger, times(1)).warn("MyTestId does not have location data");
    }

}
