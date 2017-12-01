package com.graphai.channelBased;

import com.graphai.channelBased.channels.Message;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class ApplicationProcess {

    public static final String PROCESS_ID = "59d4edea-d6a2-11e7-9296-cec278b6b50a";
    public static final String MAIN_CLASS = ApplicationProcess.class.getSimpleName();

    public ApplicationProcess() {

    }

    public static void main(String[] args) {
        // Java 9 process api
        /*
        ProcessHandle.allProcesses().forEach( p -> System.out.printf("%s %d",
                p.info().commandLine().orElse("<commandLine is empty>"),
                p.info().startInstant().orElse(Instant.MIN).toEpochMilli()));
        */

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
            for (int i = 0; i < 2; i++) {
                bw.write(PROCESS_ID, 0, PROCESS_ID.length());
                bw.newLine();
                bw.flush();
            }
/*
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for (int i = 0; i < 1; i++) {
                String line = br.readLine();
                if(Strings.isNullOrEmpty(line))
                    throw new UnsupportedOperationException("handshake failed, expecting process id");
            }
*/
            ObjectInputStream iis = new ObjectInputStream(System.in);
            try {
                Object players = iis.readObject();
                System.out.println(players.toString());
                System.out.println(players.getClass().getSimpleName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            ObjectOutputStream ois = new ObjectOutputStream(System.out);
            Message ping = Message.ping(UUID.randomUUID());
            Message reply = Message.reply(ping);
            for (int i = 0; i < 2; i++) {
                if(i == 0)
                    ois.writeObject(ping);
                if(i == 1)
                    ois.writeObject(reply);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
