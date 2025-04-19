package com.github.nikita.sakharin.conwaysgameoflife;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringRunner.class)
public final class GameTest {
    public static final int TIMES = 10;

    @Autowired
    private Game game;

    @Autowired
    private Generation generation;

    @Autowired
    private Runnable runnable;

    @Test
    public final void testRun() {
        doNothing().when(generation).close();
        when(generation.getNextGeneration(isNotNull())).thenReturn(generation);
        var stubber = doNothing();
        for (var i = 2; i < TIMES; ++i)
            stubber = stubber.doNothing();
        stubber.doThrow(RuntimeException.class).when(runnable).run();

        assertSame(null,
            assertThrows(RuntimeException.class, game::run).getMessage()
        );

        verify(generation, times(TIMES)).close();
        verify(generation, times(TIMES)).getNextGeneration(isNotNull());
        verifyNoMoreInteractions(generation);

        verify(runnable, times(TIMES)).run();
        verifyNoMoreInteractions(runnable);
    }
}
