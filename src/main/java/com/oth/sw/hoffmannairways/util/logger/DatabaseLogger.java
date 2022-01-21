package com.oth.sw.hoffmannairways.util.logger;

public class DatabaseLogger implements LoggerIF {
    //Source: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void log(String module, String message) {
        System.out.println(ANSI_BLUE + "[" + module + "]: " + message + ANSI_RESET);
    }
}
