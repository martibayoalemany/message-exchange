package com.messageExchange;

import com.messageExchange.singleProcess.recursive.RecursiveRunner;

public class InProcStarter {
    private static final int LARGE = 100_000;

    public static void main(String[] args) {
        try {
            new RecursiveRunner(LARGE).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
