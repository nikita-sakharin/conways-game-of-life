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

import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Stream;
import static java.util.Map.entry;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Stream.concat;

public final class Generation implements AutoCloseable {
    private final Stream<Cell> liveCells;

    private Generation() {
        throw new UnsupportedOperationException();
    }

    public Generation(final Stream<Cell> liveCells) {
        this.liveCells = liveCells;
    }

    private static final boolean filter(
        final Entry<Cell, Entry<Boolean, Integer>> entry
    ) {
        final var value = entry.getValue();
        final var count = value.getValue();
        return count == 3 || (value.getKey() && count == 2);
    }

    private static final Entry<Boolean, Integer> mapper(
        final Entry<Cell, Boolean> entry
    ) {
        final var isAlive = entry.getValue();
        return entry(isAlive, isAlive ? 0 : 1);
    }

    private static final Entry<Boolean, Integer> operator(
        final Entry<Boolean, Integer> a,
        final Entry<Boolean, Integer> b
    ) {
        return entry(a.getKey() || b.getKey(), a.getValue() + b.getValue());
    }

    private final Stream<Entry<Cell, Boolean>> getCellsWithNeighbours() {
        return liveCells.flatMap(cell -> concat(
            Stream.of(entry(cell, true)),
            cell.getNeighbours().map(neighbour -> entry(neighbour, false))
        ));
    }

    @Override
    public final void close() {
        liveCells.close();
    }

    public final void forEach(final Consumer<? super Cell> action) {
        liveCells.forEach(action);
    }

    public final Generation getNextGeneration(
        final Consumer<? super Cell> action
    ) {
        final var cellsWithNeighbours = getCellsWithNeighbours();
        final var collector = groupingBy(Entry::getKey, reducing(
            entry(false, 0),
            Generation::mapper,
            Generation::operator
        ));
        final var cellsInfo = cellsWithNeighbours.collect(collector);
        return new Generation(cellsInfo.entrySet().stream().filter(
            Generation::filter
        ).map(Entry::getKey).peek(action));
    }
}
