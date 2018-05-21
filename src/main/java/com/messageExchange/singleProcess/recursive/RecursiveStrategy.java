package com.messageExchange.singleProcess.recursive;

public class RecursiveStrategy {

    private Integer totalMessages;
    private boolean isVerbose;

    private RecursiveStrategy() {

    }

    // region setters
    public static RecursiveStrategy get() {
        return new RecursiveStrategy();
    }

    public RecursiveStrategy totalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public RecursiveStrategy verbose(boolean isVerbose) {
        this.isVerbose = isVerbose;
        return this;
    }

    public RecursiveStrategy build() {
        if(this.totalMessages == null)
            throw new RuntimeException("totalMessages missing");
        return this;
    }

    // endregion

    // region getters

    public Integer getTotalMessages() {
        return totalMessages;
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    // endregion

}
