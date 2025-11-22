/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nikita.sakharin.conwaysgameoflife;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringRunner.class)
public final class GameTest {
    private static final int TIMES = 10;

    @Autowired
    private Game game;

    @Autowired
    private Generation generation;

    @Autowired
    private Runnable runnable;

    @Test
    public final void testRun() {
        doNothing().when(generation).close();
        when(generation.getNextGeneration(isNotNull())).thenReturn(generation);
        var stubber = doNothing();
        for (var i = 2; i < TIMES; ++i)
            stubber = stubber.doNothing();
        stubber.doThrow(RuntimeException.class).when(runnable).run();

        assertSame(null,
            assertThrows(RuntimeException.class, game::run).getMessage()
        );

        verify(generation, times(TIMES)).close();
        verify(generation, times(TIMES)).getNextGeneration(isNotNull());
        verifyNoMoreInteractions(generation);

        verify(runnable, times(TIMES)).run();
        verifyNoMoreInteractions(runnable);
    }
}
