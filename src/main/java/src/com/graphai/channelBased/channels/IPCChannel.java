package com.graphai.channelBased.channels;

import com.graphai.channelBased.Application;
import com.graphai.channelBased.ApplicationProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Optional;

/**
 * - It implements a custom protocol to communicate between two processes
 * - It uses only stdin and stout without additional java libraries
 */
public abstract class IPCChannel implements IChannel {
    private Process process;
    private InputStream stdin;
    private OutputStream stdout;
    private String remoteId;
    private boolean isHandshakeDone;

    abstract void handleMessageLoop();

    public InputStream getInputStream() {
        return stdin;
    }

    OutputStream getOutputStream() {
        return stdout;
    }

    /**
     * @return and already initialized object outputstream after a isHandshakeDone
     */
    Optional<ObjectOutputStream> getObjectOutputStream() {
        if(!isHandshakeDone) return Optional.empty();
        if(stdout == null) return Optional.empty();
        try {
            return Optional.of(new ObjectOutputStream(stdout));
        } catch (IOException ignore) {
            return Optional.empty();
        }
    }

    /**
     * Initializes a new process and executes a isHandshakeDone
     * @param mainClass The main class to be started in a new process
     * @param processId The process id as a string
     * @return -1 if it failed
     */
    public int init(String mainClass, String processId) {
        if(process != null)
            throw new UnsupportedOperationException();
        String javaCmd = System.getProperty("java.home").concat("\\..\\bin\\java");
        String classpath = System.getProperty("java.class.path");

        try {
            process = new ProcessBuilder()
                    .command(javaCmd, "-cp", classpath, ApplicationProcess.MAIN_CLASS)
                    .redirectInput(ProcessBuilder.Redirect.PIPE)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start();
        } catch (IOException ignore) {
            return -1;
        }
        return handshake(processId, process.getInputStream(), process.getOutputStream());
    }

    /**
     * It wraps the process, waits and catches the InterruptedException should we have one
     * @see java.lang.Process#waitFor
     */
    public int waitFor() {
        try {
            return process.waitFor();
        } catch (InterruptedException ignore) {
            return -1;
        }
    }

    /**
     * It executes a isHandshakeDone protocol using the process id
     * @param processId a string with a custom id for the process
     * @param stdin inputstream of the process already started
     * @param stdout outputstream of the process already started
     * @return -1 if it did not succeeded
     */
    int handshake(String processId, InputStream stdin, OutputStream stdout) {
        // Start isHandshakeDone
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdout))) {
            Thread t = new Thread(() -> {
                try {
                    Thread.currentThread().setName("handshake-of-"+ ApplicationProcess.PROCESS_ID);
                    int handshakeCount = 0;
                    // Check we can read the process id two times in a row
                    while (handshakeCount < 2) {
                        String line = reader.readLine();
                        if (line != null) {
                            if (line.contains(ApplicationProcess.PROCESS_ID))
                                handshakeCount++;
                            System.out.printf("%d %d %s \n", handshakeCount, line.length(), line);
                            writer.write(Application.PROCESS_ID);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });
            t.start();
            t.join();
        } catch (InterruptedException | IOException ignore) {
            return -1;
        }
        this.stdin = stdin;
        this.stdout = stdout;
        this.remoteId = processId;
        return 0;
    }

    public boolean send(Object obj) {
        if(!isHandshakeDone)
            return false;
        if (stdout == null)
            return false;
        if (stdin == null)
            return false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
             ObjectOutputStream oos = new ObjectOutputStream((stdout))){
            oos.writeObject(obj);
            String line = reader.readLine();
            System.out.printf("%s", line);
            line = reader.readLine();
            System.out.printf("%s", line);
        } catch (IOException ignore) {
            return false;
        }
        return true;
    }
}
