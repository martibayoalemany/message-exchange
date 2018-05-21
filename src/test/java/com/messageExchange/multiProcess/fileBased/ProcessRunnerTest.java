package com.messageExchange.multiProcess.fileBased;

import org.junit.Test;

public class ProcessRunnerTest {
    @Ignore
    @Test
    public void execute() throws Exception {
        ProcessRunner pr = new ProcessRunner();
        pr.execute(10, 10, true);
    }

}
