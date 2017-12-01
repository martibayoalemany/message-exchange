package com.graphai.channelBased;

import com.graphai.channelBased.channels.IChannel;
import com.graphai.channelBased.channels.IPCChannel;
import com.graphai.channelBased.channels.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Application {
    private static final int PINGS_TOTAL = 10_000;
    public static final String PROCESS_ID = "59d4f09c-d6a2-11e7-9296-cec278b6b50a";
    private final Player[] players;
    private IPCChannel ipcChannel;

    Application(IPCChannel ipcChannel) {
        players = new Player[]{
                Player.pingAndStopAfter(PINGS_TOTAL, false,
                        (s) -> s.getRepliesReceivedCount() >= PINGS_TOTAL, ipcChannel),
                Player.stopAfter(
                        (s) -> s.getPingsReceivedCount() >= PINGS_TOTAL && s.getRepliesSentCount() >= PINGS_TOTAL, ipcChannel)};
        this.ipcChannel = ipcChannel;
    }

    Application(IChannel channel) {
        this(channel, false);
    }

    Application(IChannel channel, boolean sentOnePacketPerLoop) {
        players = new Player[]{
                Player.pingAndStopAfter(PINGS_TOTAL, sentOnePacketPerLoop, (s) -> s.getRepliesReceivedCount() >= PINGS_TOTAL, channel),
                Player.stopAfter((s) -> s.getPingsReceivedCount() >= PINGS_TOTAL && s.getRepliesSentCount() >= PINGS_TOTAL, channel)};
    }

    void execute() {
        long start = Instant.now().toEpochMilli();
        List<Thread> threadList = Arrays.stream(players).map(Thread::new).collect(Collectors.toList());
        threadList.forEach(Thread::start);
        int finishedThreads = 0;
        while (finishedThreads < threadList.size()) {
            for (Thread t : threadList) {
                try {
                    t.join();
                    finishedThreads++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Logger.get().log("Executed : %d ms", (Instant.now().toEpochMilli() - start));
        Logger.get().log("-------");
    }

    void executeSingleThreaded() {
        long start = Instant.now().toEpochMilli();
        List<Player> players = Arrays.stream(this.players).collect(Collectors.toList());
        while (!players.stream().allMatch(Player::isFinished))
            for (Player player : players)
                if (!player.isFinished())
                    player.mainLoop();

        Logger.get().log("Executed : %d ms", (Instant.now().toEpochMilli() - start));
        Logger.get().log("-------");
    }

    private void executeProcess() {
        this.ipcChannel.init(ApplicationProcess.MAIN_CLASS, ApplicationProcess.PROCESS_ID);
        this.ipcChannel.send(players[0]);

        Thread messageHandler = new Thread(() ->{
            while(true)
            {
            ObjectInputStream iis = null;
            try {
                iis = new ObjectInputStream(ipcChannel.getInputStream());
                Object obj = iis.readObject();
                Message msg = (Message) obj;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }});

        this.ipcChannel.waitFor();
        messageHandler.interrupt();
    }

    public static void main(String[] args) {
        Logger.get().log("------- execute process --------");
        new Application(Channels.getIpc()).executeProcess();
    }
}
