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

import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Profile("!test")
public final class Game implements Runnable {
    private static final Logger log = getLogger(Game.class);

    private Generation generation;
    private final Optional<Runnable> runnable;

    public Game(
        final Generation firstGeneration,
        @Qualifier("runnable") final Optional<Runnable> runnable
    ) {
        this.generation = firstGeneration;
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        for (var iteration = 0L; ; ++iteration)
            try (final var previous = generation) {
                generation = previous.getNextGeneration(cell ->
                    log.info(cell.toString())
                );
                runnable.ifPresent(Runnable::run);
                log.info("iteration: {}", iteration);
            }
    }
}
