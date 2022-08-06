package com.github.nikita.sakharin.conwaysgameoflife;

import static java.lang.Integer.parseInt;
import static java.util.Objects.hash;
import static java.util.stream.IntStream.rangeClosed;
import java.util.stream.Stream;

public final class Cell {
    private static final int RADIX = 10;

    private int i, j;

    public Cell(final int i, final int j) {
        this.i = i;
        this.j = j;
    }

    public static Cell of(final int i, final int j) {
        return new Cell(i, j);
    }

    public static Cell parseCell(final String string) {
        final var split = string.split(";");
        if (!split[0].startsWith("(") || !split[1].endsWith(")"))
            throw new IllegalArgumentException(string);
        final var i = parseInt(split[0], 1, split[0].length(), RADIX);
        final var j = parseInt(split[1], 0, split[1].length() - 1, RADIX);
        return Cell.of(i, j);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final Cell cell = (Cell) obj;
        return i == cell.i && j == cell.j;
    }

    @Override
    public final int hashCode() {
        return hash(i, j);
    }

    @Override
    public final String toString() {
        return "Cell(" + i + "," + j + ")";
    }

    public final int getI() {
        return i;
    }

    public final int getJ() {
        return j;
    }

    public final void setI(final int i) {
        this.i = i;
    }

    public final void setJ(final int j) {
        this.j = j;
    }

    public final Stream<Cell> getNeighbours() {
        return rangeClosed(i - 1, i + 1).boxed().flatMap(a ->
            rangeClosed(j - 1, j + 1).mapToObj(b -> Cell.of(a, b))
        ).filter(cell -> !equals(cell));
    }
}
