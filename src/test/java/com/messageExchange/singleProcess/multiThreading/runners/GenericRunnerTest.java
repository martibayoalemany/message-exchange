package com.messageExchange.singleProcess.multiThreading.runners;

import com.messageExchange.singleProcess.multiThreading.GenericStrategy;
import com.messageExchange.singleProcess.multiThreading.channels.*;
import com.messageExchange.singleProcess.multiThreading.enums.Locking;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class GenericRunnerTest {
    private static final int BUFFER = 20;

    public static @DataPoints
    GenericStrategy[] straegies =  {
            GenericStrategy.get()
                    .totalMessages(20)
                    .bufferSize(BUFFER)
                    .locking(Locking.Fair)
                    .channel(new ArrayBasedChannel(BUFFER))
                    .verbose(true)
                    .build(),
            GenericStrategy.get()
                    .totalMessages(20)
                    .bufferSize(BUFFER)
                    .locking(Locking.Unfair)
                    .channel(new FileBasedChannel(BUFFER))
                    .verbose(true)
                    .build(),
            GenericStrategy.get()
                    .totalMessages(20)
                    .bufferSize(BUFFER)
                    .locking(Locking.None)
                    .channel(new MemoryChannel(BUFFER))
                    .verbose(true)
                    .build(),
            GenericStrategy.get()
                    .totalMessages(20)
                    .bufferSize(BUFFER)
                    .locking(Locking.None)
                    .channel(new AtomicChannel(BUFFER))
                    .verbose(true)
                    .build(),
            GenericStrategy.get()
                    .totalMessages(20)
                    .bufferSize(BUFFER)
                    .locking(Locking.Unfair)
                    .channel(new DequeChannel(BUFFER))
                    .verbose(true)
                    .build(),
    };


    @Theory
    public void execute(GenericStrategy strategy) throws Exception {
        GenericRunner runner = new GenericRunner(strategy);
        assertTrue(runner.execute());
    }

}