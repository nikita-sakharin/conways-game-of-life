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

import java.util.stream.Stream;
import static java.lang.Integer.parseInt;
import static java.util.Objects.hash;
import static java.util.stream.IntStream.rangeClosed;

public final class Cell {
    private static final int RADIX = 10;

    private int i, j;

    public Cell(final int i, final int j) {
        this.i = i;
        this.j = j;
    }

    public static final Cell of(final int i, final int j) {
        return new Cell(i, j);
    }

    public static final Cell parse(final String string) {
        final var split = string.split(";");
        if (!split[0].startsWith("(") || !split[1].endsWith(")"))
            throw new IllegalArgumentException(string);
        final var i = parseInt(split[0], 1, split[0].length(), RADIX);
        final var j = parseInt(split[1], 0, split[1].length() - 1, RADIX);
        return Cell.of(i, j);
    }

    @Override
    public final boolean equals(final Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        final var cell = (Cell) object;
        return i == cell.i && j == cell.j;
    }

    @Override
    public final int hashCode() {
        return hash(i, j);
    }

    @Override
    public final String toString() {
        return "Cell(i=" + i + ", j=" + j + ")";
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
