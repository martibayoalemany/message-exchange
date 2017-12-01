package com.graphai.simple;

import java.io.IOException;

public class Utils {

    public static Process execute(Class clazz) {
        Process result = null;
        String javaCmd = System.getProperty("java.home").concat("\\..\\bin\\java");
        String classpath = System.getProperty("java.class.path");

        try {
            result = new ProcessBuilder()
                    .command(javaCmd, "-cp", classpath, clazz.getName())
                    .redirectInput(ProcessBuilder.Redirect.PIPE)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start();
        } catch (IOException ignore) {
            return null;
        }
        return result;
    }
}
