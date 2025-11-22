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

import java.util.List;
import org.junit.Test;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import static com.github.nikita.sakharin.conwaysgameoflife.Cell.parse;

public final class CellTest {
    private static final int LIMIT = 64;

    @Test
    public final void testGetNeighbours() {
        for (int i = -LIMIT; i < LIMIT; ++i)
            for (int j = -LIMIT; j < LIMIT; ++j)
                assertEquals(List.of(
                    Cell.of(i - 1, j - 1), Cell.of(i - 1, j),
                    Cell.of(i - 1, j + 1), Cell.of(i, j - 1),
                    Cell.of(i, j + 1), Cell.of(i + 1, j - 1),
                    Cell.of(i + 1, j), Cell.of(i + 1, j + 1)
                ), Cell.of(i, j).getNeighbours().collect(toList()));
    }

    @Test
    public final void testOf() {
        for (int i = -LIMIT; i < LIMIT; ++i)
            for (int j = -LIMIT; j < LIMIT; ++j)
                assertEquals(new Cell(i, j), Cell.of(i, j));
    }

    @Test
    public final void testParse() {
        for (int i = -LIMIT; i < LIMIT; ++i)
            for (int j = -LIMIT; j < LIMIT; ++j)
                assertEquals(Cell.of(i, j), parse("(" + i + ";" + j + ")"));
    }
}
