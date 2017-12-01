package com.graphai.channelBased;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class Logger {

    private volatile OutputStream os;
    private volatile BufferedOutputStream bufferedOutputStream;
    private volatile boolean isEnabled;

    static class LOGGER {
        static Logger instance = new Logger();
    }

    static Logger get() {
        return LOGGER.instance;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private Logger() {
        isEnabled = true;
    }

    synchronized void log(String format, Object... params) {
        log(String.format(format.concat("\n"), params));

    }

    private void log(String msg) {
        if(this.bufferedOutputStream == null && isEnabled) {
            Path outputFile = Paths.get("target/logging.txt");
            try {
                this.os = (outputFile.toFile().exists()) ?
                        Files.newOutputStream(outputFile, StandardOpenOption.APPEND) :
                        Files.newOutputStream(outputFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);
                this.bufferedOutputStream = new BufferedOutputStream(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(this.bufferedOutputStream != null) {
            try {
                String message = String.format("%d - %s", System.currentTimeMillis(), msg);
                bufferedOutputStream.write(message.getBytes());
                bufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
