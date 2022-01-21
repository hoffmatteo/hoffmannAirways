package com.oth.sw.hoffmannairways.util.logger;

public class ErrorLogger implements LoggerIF {
    //Source: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void log(String module, String message) {
        System.out.println(ANSI_RED + "[" + module + "]: ERROR " + message + ANSI_RESET);
    }
}
