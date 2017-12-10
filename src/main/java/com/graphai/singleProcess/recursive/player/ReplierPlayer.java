package com.graphai.singleProcess.recursive.player;

import com.graphai.messages.ByteMessage;
import com.graphai.singleProcess.IPlayer;
import com.graphai.singleProcess.multiThreading.GenericStrategy;
import com.graphai.singleProcess.recursive.RecursiveStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ReplierPlayer implements  IPlayer, PropertyChangeListener {
    private final RecursiveStrategy strategy;
    private int repliesCount;

    public ReplierPlayer(RecursiveStrategy strategy) {
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
        return 0;
    }

    @Override
    public int getRepliesCount() {
        return repliesCount;
    }

    @Override
    public boolean hasFinished() {
        return repliesCount >= strategy.getTotalMessages();
    }

    @Override
    public int getBufferSize() {
        return -1;
    }

    @Override
    public IPlayer call() throws Exception {
        return this;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if((evt.getPropertyName().equals("value")) && ((byte)evt.getNewValue() == ByteMessage.PING.getValue())) {
            if(isVerbose())
                System.out.println(getRepr());
            ((InitiatorPlayer)evt.getSource()).setValue(ByteMessage.REPLY.getValue());
            repliesCount ++;
        }
    }
}