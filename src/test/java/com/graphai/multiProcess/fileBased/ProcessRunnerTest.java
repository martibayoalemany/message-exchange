package com.graphai.multiProcess.fileBased;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Ignore;
import org.junit.Test;

public class ProcessRunnerTest {
    @Ignore
    @Test
    public void execute() throws Exception {
        ProcessRunner pr = new ProcessRunner();
        pr.execute(10, 10, true);
    }

}
