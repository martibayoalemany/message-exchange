package com.graphai.multiProcess.fileBased;

import com.graphai.utils.ProcessUtils;

import java.io.IOException;


/**
 * It runs two players in two threads and exchanges *totalMessage* ping and reply messages
 * using a MemoryMap
 */
public class ProcessRunner implements IProcessRunner {

    public ProcessRunner() {

    }

    public void execute(int totalMessages, int buffer, boolean verbose) {
        try {
            Process initiator = execute(InitiatorProcess.class, totalMessages, buffer, verbose);
            Process replier = execute(ReplierProcess.class, totalMessages, buffer, verbose);

            long initiatorPid = ProcessUtils.getPidOfProcess(initiator);
            long replierPid = ProcessUtils.getPidOfProcess(replier);
            System.out.printf("[Initiator pid]: %5d / [Replier pid] : %5d\n", initiatorPid, replierPid);
            initiator.waitFor();
            replier.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Process execute(Class clazz, Integer totalMessages, Integer bufferSize, boolean verbose) throws IOException {
        return ProcessUtils.command(clazz,
                String.valueOf(totalMessages),
                String.valueOf(bufferSize),
                String.valueOf(verbose))
                .redirectErrorStream(true)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
    }
}

