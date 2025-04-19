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
