package com.graphai.simple;

import com.graphai.simple.entities.SimpleMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * TODO: Finish implementation System.in and System.out block mutually
 */
@Deprecated
public class ProcessRunner {
    @SuppressWarnings("WeakerAccess")
    public static final int PING_TOTAL = 10;

    void execute() {
        // starts a separate process from a jar with the ProcessMain class
        try {
            Process pr = Utils.execute(ProcessMain.class);
            if (pr == null)
                return;
            Initiator initiator = new Initiator(pr, UUID.randomUUID(), UUID.randomUUID());
            Thread t = new Thread(initiator);
            t.start();
            // Waits the process to finish
            pr.waitFor();
            // Waits the initiatior to finish
            t.join();
        } catch (InterruptedException ignore) {

        }
    }

    public class Initiator implements Runnable {

        private final Process process;
        private final UUID initiatorId;
        private final UUID listenerId;

        private int repliesCount = 0;

        Initiator(Process process, UUID initiatorId, UUID listenerId) {
            this.process = process;
            this.initiatorId = initiatorId;
            this.listenerId = listenerId;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(String.valueOf(initiatorId));
            try {
                // Handshake
                /*
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while (true) {
                    String line = br.readLine();
                    if (line.equals(ProcessMain.PROCESS_ID))
                        break;
                    Thread.sleep(200);
                }
                */

                // Initiate
                ObjectOutputStream ois = new ObjectOutputStream(process.getOutputStream());
                for (int i = 0; i < PING_TOTAL; i++)
                    ois.writeObject(SimpleMessage.ping(initiatorId, listenerId));

                // Read packages
                ObjectInputStream iis = new ObjectInputStream(process.getInputStream());
                while (repliesCount < PING_TOTAL) {
                    SimpleMessage message = (SimpleMessage) iis.readObject();
                    if (message.getKind() == SimpleMessage.Kind.REPLY)
                        repliesCount++;
                }

            } catch (IOException | ClassCastException | ClassNotFoundException e) {
                throw new UnsupportedOperationException(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        new ProcessRunner().execute();
        System.out.printf(" %s %d ms", ProcessRunner.class.getSimpleName(), System.currentTimeMillis() - start);
    }
}
