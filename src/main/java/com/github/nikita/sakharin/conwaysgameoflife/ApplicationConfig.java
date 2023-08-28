package com.github.nikita.sakharin.conwaysgameoflife;

import java.util.Scanner;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@Configuration
@PropertySource("classpath:first-generation.properties")
public class ApplicationConfig {
    private static final Scanner SCANNER = new Scanner(System.in);

    @Bean
    @Profile("!test")
    public Generation firstGeneration(@Value("${cells}") final String[] cells) {
        return new Generation(Stream.of(cells).map(Cell::parse));
    }

    @Bean("runnable")
    @Profile("auto")
    public Runnable auto() {
        return () -> System.out.println("Next iteration starts...");
    }

    @Bean("runnable")
    @Profile("manual")
    public Runnable manual() {
        return () -> {
            System.out.print("Press Enter to continue...");
            SCANNER.nextLine();
        };
    }
}
