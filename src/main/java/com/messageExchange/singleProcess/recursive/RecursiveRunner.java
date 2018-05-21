package com.messageExchange.singleProcess.recursive;

import com.messageExchange.singleProcess.IPlayer;
import com.messageExchange.singleProcess.recursive.player.InitiatorPlayer;
import com.messageExchange.singleProcess.recursive.player.ReplierPlayer;

public class RecursiveRunner {
    private RecursiveStrategy strategy;
    private boolean hasFinished;

    public RecursiveRunner(int totalMessages) {
        this.strategy = RecursiveStrategy.get().totalMessages(totalMessages).build();
    }

    public RecursiveRunner(RecursiveStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute() throws Exception {
        InitiatorPlayer initiatorPlayer = new InitiatorPlayer(strategy);
        ReplierPlayer replierPlayer = new ReplierPlayer(strategy);
        initiatorPlayer.addPropertyChangedListener(replierPlayer);
        IPlayer result = initiatorPlayer.call();
        this.hasFinished = result.hasFinished();
    }

    public boolean hasFinished() {
        return hasFinished;
    }
}
