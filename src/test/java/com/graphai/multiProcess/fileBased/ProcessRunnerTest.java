package com.graphai.multiProcess.fileBased;

import org.junit.Test;

public class ProcessRunnerTest {
    @Test
    public void execute() throws Exception {
        ProcessRunner pr = new ProcessRunner();
        pr.execute(10, 10, true);
    }

}