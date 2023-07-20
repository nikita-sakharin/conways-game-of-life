package com.github.nikita.sakharin.conwaysgameoflife;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class Main {
    public static void main(final String[] args) {
        try (final var context =
            new AnnotationConfigApplicationContext(ApplicationConfig.class)
        ) {
            context.getBean("game", Game.class).run();
        }
    }
}
