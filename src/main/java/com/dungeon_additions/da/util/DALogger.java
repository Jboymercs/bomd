package com.dungeon_additions.da.util;

import com.google.common.collect.Lists;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Credit goes to SmileyCorps for this quick log class to catch any misread biomes
 */
public class DALogger {
    private static boolean has_errors;
    private static Path log_file = Paths.get("logs/da.log");
    public static void logError(Object message, Exception e) {
        writeToFile(message + " " + e);
        for (StackTraceElement traceElement : e.getStackTrace()) writeToFile(traceElement);
        e.printStackTrace();
        has_errors = true;
    }

    public static void clearLog() {
        try {
            Files.write(log_file, Lists.newArrayList(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean writeToFile(Object message) {
        return writeToFile(Lists.newArrayList(String.valueOf(message)));
    }
    private static boolean writeToFile(List<String> out) {
        try {
            Files.write(log_file, out, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasErrors() {
        return has_errors;
    }
}
