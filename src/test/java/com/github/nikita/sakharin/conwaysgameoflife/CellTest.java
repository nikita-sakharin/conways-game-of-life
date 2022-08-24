package com.github.nikita.sakharin.conwaysgameoflife;

import java.util.List;
import org.junit.Test;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import static com.github.nikita.sakharin.conwaysgameoflife.Cell.parseCell;

public final class CellTest {
    private static final int LIMIT = 64;

    @Test
    public final void testGetNeighbours() {
        for (int i = -LIMIT; i <= LIMIT; ++i)
            for (int j = -LIMIT; j <= LIMIT; ++j)
                assertEquals(List.of(
                    Cell.of(i - 1, j - 1), Cell.of(i - 1, j),
                    Cell.of(i - 1, j + 1), Cell.of(i, j - 1),
                    Cell.of(i, j + 1), Cell.of(i + 1, j - 1),
                    Cell.of(i + 1, j), Cell.of(i + 1, j + 1)
                ), Cell.of(i, j).getNeighbours().collect(toList()));
    }

    @Test
    public final void testOf() {
        for (int i = -LIMIT; i <= LIMIT; ++i)
            for (int j = -LIMIT; j <= LIMIT; ++j)
                assertEquals(new Cell(i, j), Cell.of(i, j));
    }

    @Test
    public final void testParseCell() {
        for (int i = -LIMIT; i <= LIMIT; ++i)
            for (int j = -LIMIT; j <= LIMIT; ++j)
                assertEquals(Cell.of(i, j), parseCell("(" + i + ";" + j + ")"));
    }
}
