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

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Test;
import static java.util.Map.entry;
import static java.util.function.Function.identity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class GenerationTest {
    private static final int LENGTH = 3, COUNT = 1 << LENGTH * LENGTH;

    private static final Stream<Cell> createLiveCells(final int index) {
        final var set = BitSet.valueOf(new long[] { index });
        return set.stream().mapToObj(GenerationTest::indexToCell);
    }

    private static final Set<Cell> getExpectedNextGeneration(
        final Stream<Cell> liveCells
    ) {
        final Map<Cell, Entry<Boolean, Integer>> cellInfo = new HashMap<>();
        liveCells.forEach(cell -> {
            cell.getNeighbours().forEach(neighbour ->
                cellInfo.merge(neighbour, entry(false, 1), (value, unused) ->
                    entry(value.getKey(), value.getValue() + 1)
                )
            );
            cellInfo.merge(cell, entry(true, 0), (value, unused) ->
                entry(true, value.getValue())
            );
        });
        cellInfo.entrySet().removeIf(entry -> {
            final var value = entry.getValue();
            final var count = value.getValue();
            return count != 3 && (!value.getKey() || count != 2);
        });
        return cellInfo.keySet();
    }

    private static final Cell indexToCell(final int index) {
        return Cell.of(index / LENGTH, index % LENGTH);
    }

    @Test
    public final void testGetNextGeneration() {
        final var action = identity();
        for (int i = 0; i < COUNT; ++i) {
            final var expected = getExpectedNextGeneration(createLiveCells(i));
            final Set<Cell> actual = new HashSet<>();
            new Generation(createLiveCells(i)).getNextGeneration(cell ->
                assertTrue(actual.add(cell))
            ).forEach(action::apply);
            assertEquals(expected, actual);
        }
    }
}
