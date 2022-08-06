package com.github.nikita.sakharin.conwaysgameoflife;

import java.util.Optional;
import static org.mockito.Mockito.mock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@ComponentScan
@Configuration
public class TestConfig {
    @Bean
    public Game game(
        final Generation generation,
        @Qualifier("runnable") final Optional<Runnable> runnable
    ) {
        return new Game(generation, runnable);
    }

    @Bean
    @Profile("test")
    public Generation generation() {
        return mock(Generation.class);
    }

    @Bean("runnable")
    @Profile("test")
    public Runnable runnable() {
        return mock(Runnable.class);
    }
}
