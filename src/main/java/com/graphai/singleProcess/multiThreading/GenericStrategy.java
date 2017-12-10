package com.graphai.singleProcess.multiThreading;

import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.channels.*;
import com.graphai.singleProcess.multiThreading.enums.Locking;
import com.graphai.singleProcess.multiThreading.enums.PlayerBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class GenericStrategy {

    private volatile ReentrantLock lock;
    private boolean isBuilt = false;
    private List<IPlayer> players = new ArrayList<>();
    private ICustomChannel channel;
    private int bufferSize = 1;
    private int numThreads = 2;
    private Integer totalMessages = 0;
    private Locking locking = Locking.None;
    private PlayerBehaviour playerBehaviour = PlayerBehaviour.Both;
    private boolean isVerbose = false;
    private final static int BUFFER = 20;

    protected GenericStrategy() {

    }

    // region setters
    public static GenericStrategy get() {
        return new GenericStrategy();
    }

    public GenericStrategy verbose(boolean isVerbose) {
        this.isVerbose = isVerbose;
        return this;
    }

    public GenericStrategy totalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
        return this;
    }

    public GenericStrategy numThreads(int numThreads) {
        this.numThreads = numThreads;
        return this;
    }

    public GenericStrategy bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public GenericStrategy locking(Locking locking) {
        this.locking = locking;
        return this;
    }

    public GenericStrategy channel(ICustomChannel channel) {
        this.channel = channel;
        return this;
    }

    public GenericStrategy select(PlayerBehaviour playerBehaviour) {
        if(this.playerBehaviour != PlayerBehaviour.Both)
            throw new RuntimeException("Only one behaviour per strategy is allowed");
        this.playerBehaviour = playerBehaviour;
        return this;
    }

    public GenericStrategy build() {
        if(this.totalMessages == null)
            throw new RuntimeException("totalMessages missing");

        if(this.channel == null) {
            this.channel = new ArrayBasedChannel(bufferSize);
        }
        if(this.locking != null && this.locking != Locking.None) {
            this.lock = new ReentrantLock(locking == Locking.Fair);
        }
        if(this.players.isEmpty()) {
            List<IPlayer> players = new ArrayList<>();
            if(playerBehaviour == PlayerBehaviour.Initiator || playerBehaviour == PlayerBehaviour.Both)
                players.add(this.channel.createInitiator(this));
            if(playerBehaviour == PlayerBehaviour.Replier || playerBehaviour == PlayerBehaviour.Both)
                players.add(this.channel.createReplier(this));
            this.players.addAll(players);
        }

        this.isBuilt = true;
        return this;
    }
    // endregion

    // region getters


    public boolean isVerbose() {
        return isVerbose;
    }

    public List<IPlayer> getPlayers() {
        return players;
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    public ReentrantLock getLock() {
        if(!this.isBuilt)
            throw new RuntimeException("build() was not yet called");
        return lock;
    }

    public int getBufferSize() {
        if(!this.isBuilt)
            throw new RuntimeException("build() was not yet called");
        return bufferSize;
    }

    public int getNumThreads() {
        if(!this.isBuilt)
            throw new RuntimeException("build() was not yet called");
        return numThreads;
    }

    public int getTotalMessages() {
        if(!this.isBuilt)
            throw new RuntimeException("build() was not yet called");
        return totalMessages;
    }

    public ICustomChannel getChannel() {
        if(!this.isBuilt)
            throw new RuntimeException("build() was not yet called");
        return channel;
    }
    // endregion getters
}

