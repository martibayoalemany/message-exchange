package com.messageExchange;

import com.messageExchange.multiProcess.fileBased.ProcessRunner;

public class OutProcStarter {
    private static final int LARGE = 100_000;

    public static void main(String[] args) {
        // TODO: there is still some work to do this as fast as the InProc version
        new ProcessRunner().execute(LARGE, 20, true);
    }
}
