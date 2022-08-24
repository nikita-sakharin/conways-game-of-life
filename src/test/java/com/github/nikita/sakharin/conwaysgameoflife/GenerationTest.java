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

    private static Stream<Cell> createLiveCells(final int index) {
        final var set = BitSet.valueOf(new long[] { index });
        return set.stream().mapToObj(GenerationTest::indexToCell);
    }

    private static Set<Cell> getExpectedNextGeneration(
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

    private static Cell indexToCell(final int index) {
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
