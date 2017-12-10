package com.graphai.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * ProcessUtils to start a class in a separate java process
 */
public class ProcessUtils {

    public static ProcessBuilder command(Class clazz, String ... params) {
        String[] fullParams = new String[params.length + 4];
        fullParams[0] = System.getProperty("java.home")
                .concat(File.separator).concat("..")
                .concat(File.separator).concat("bin")
                .concat(File.separator).concat("java");
        fullParams[1] = "-cp";
        fullParams[2] = System.getProperty("java.class.path");
        fullParams[3] = clazz.getName();
        int i = 4;
        for (String param: params) fullParams[i++] = param;
        return new ProcessBuilder()
                .command(fullParams);
    }

    public static void write(Path path, String format, Object... args) {
        String separator = System.getProperty("line.separator");
        write(path, String.format(format.concat(separator), args));
    }

    private static void write(Path path, String msg) {
        try {
            Files.write(path, msg.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {
        }
    }

    public static synchronized long getPidOfProcess(Process p) {
        long pid = -1;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                //noinspection JavaReflectionMemberAccess
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }
}
