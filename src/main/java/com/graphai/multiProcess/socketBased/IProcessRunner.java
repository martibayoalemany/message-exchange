package com.graphai.multiProcess.socketBased;

import java.io.IOException;

public interface IProcessRunner {

    Process execute(Class clazz, Integer totalMessages, Integer bufferSize, boolean verbose) throws IOException;
}
