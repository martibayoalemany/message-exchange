package com.graphai.channelBased.channels;

import com.graphai.channelBased.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * - It redirects the messages depending on the remote player id
 * - This implementation implements a custom remoting protocol without any other messaging libraries
 * TODO: to be finished
 * {@inheritDoc}
 */
@Deprecated
public  class ProcessChannel extends IPCChannel {

    private final LinkedList<Message> messagesList = new LinkedList<>();
    private final LinkedList<UUID> remotes = new LinkedList<>();

    boolean send(Player player) {
        if (super.send(player)) {
            remotes.add(player.getId());
            return true;
        }
        return false;
    }

    @Override
    public synchronized List<Message> pushAll(List<Message> messages) {
        final List<Message> result = new LinkedList<>();
        // Filter messages from a remote process from messages for a local process
        Predicate<Message> isRemote = (Message s) -> remotes.contains(s.getReceiver());
        List<Message> messagesToSend = messages.stream().filter(isRemote).collect(Collectors.toList());
        messages.removeIf(isRemote);
        if(this.messagesList.addAll(messages))
            result.addAll(messages);
        // Send messages to remote end points
        getObjectOutputStream().ifPresent(s -> {
            try {
                s.writeObject(messagesToSend);
                result.addAll(messagesToSend);
            } catch (IOException ignore) {

            }
        });
        return result;
    }

    @Override
    public synchronized List<Message> pop(Predicate<Message> filterBy) {

        return null;
    }

    @Override
    public boolean isObservable() {
        return false;
    }

    @Override
    public void handleMessageLoop() {
        Thread messageHandler = new Thread(() -> {
            while (true) {
                ObjectInputStream iis = null;
                try {
                    iis = new ObjectInputStream(getInputStream());
                    Object obj = iis.readObject();
                    Message msg = (Message) obj;
                    System.out.println(msg.toString());

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        waitFor();
        messageHandler.interrupt();
    }
}
