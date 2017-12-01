package com.graphai.simple;

import com.graphai.simple.entities.SimpleMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is started from a jar
 */
@Deprecated
public class ProcessMain {

    public static String PROCESS_ID = "251b367a-d6c7-11e7-9296-cec278b6b50a";
    private static int pingsReplied = 0;

    private static List<SimpleMessage> messageList = new LinkedList<>();

    public static void main(String[] args) {
        try {
            while (true) {
                // Handshake
            /*
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
            for (int i = 0; i < 1; i++) {
                bw.write(PROCESS_ID, 0, PROCESS_ID.length());
                bw.newLine();
                bw.flush();
            }
            */

                ObjectInputStream iis = new ObjectInputStream(System.in);
                while (messageList.size() < ProcessRunner.PING_TOTAL) {
                    SimpleMessage message = (SimpleMessage) iis.readObject();
                    messageList.add(message);
                }
                iis.close();

                ObjectOutputStream ois = new ObjectOutputStream(System.out);
                for (SimpleMessage message : messageList) {
                    if (message.getKind() == SimpleMessage.Kind.PING) {
                        ois.writeObject(SimpleMessage.reply(message));
                        ois.flush();
                        pingsReplied++;
                    }
                }

                if (pingsReplied == ProcessRunner.PING_TOTAL)
                    break;


            }
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }
}

