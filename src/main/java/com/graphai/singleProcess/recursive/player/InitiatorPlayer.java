package com.graphai.singleProcess.recursive.player;

import com.graphai.messages.ByteMessage;
import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.recursive.RecursiveStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class InitiatorPlayer implements IPlayer {
    private RecursiveStrategy strategy;
    private int pingsCount;
    private int repliesCount;
    private byte value;
    private ReplierPlayer observer;

    public InitiatorPlayer(RecursiveStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isVerbose() {
        return strategy.isVerbose();
    }

    @Override
    public int getPingsCount() {
        return pingsCount;
    }

    @Override
    public int getRepliesCount() {
        return repliesCount;
    }

    @Override
    public boolean hasFinished() {
        return pingsCount >= strategy.getTotalMessages() && repliesCount >= strategy.getTotalMessages();
    }

    @Override
    public int getBufferSize() {
        return -1;
    }

    public byte getValue() {
        return value;
    }

    public void addObserver(ReplierPlayer observer) {
        this.observer = observer;
    }

    public void setValue(byte value) {
        if(value != this.value) {
            if(value == ByteMessage.PING.getValue())
                pingsCount ++;
            if(value == ByteMessage.REPLY.getValue())
                repliesCount ++;
            byte oldValue = this.value;
            this.value = value;
            if(this.listener != null)
                this.listener.propertyChange(new PropertyChangeEvent(this, "value", oldValue, this.value));
        }
    }

    private PropertyChangeListener listener;
    public void addPropertyChangedListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public IPlayer call() throws Exception {
        while(pingsCount <= strategy.getTotalMessages()) {
            if(isVerbose())
                System.out.println(getRepr());
            setValue(ByteMessage.PING.getValue());
        }
        return this;
    }

}
