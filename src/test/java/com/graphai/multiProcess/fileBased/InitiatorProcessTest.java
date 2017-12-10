package com.graphai.multiProcess.fileBased;

import org.junit.Test;

import static org.junit.Assert.*;

public class InitiatorProcessTest {
    @Test
    public void main() throws Exception {

        String[] args = {"10", "10", "false"};
        InitiatorProcess.main(args);

    }

}