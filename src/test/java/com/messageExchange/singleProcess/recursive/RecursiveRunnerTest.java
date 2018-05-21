package com.messageExchange.singleProcess.recursive;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecursiveRunnerTest {
    @Test
    public void execute() throws Exception {
        RecursiveStrategy strategy = RecursiveStrategy.get().totalMessages(20).verbose(true).build();
        RecursiveRunner runner = new RecursiveRunner(strategy);
        runner.execute();
        assertTrue(runner.hasFinished());
    }

}