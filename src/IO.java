package src;

import java.util.Scanner;

public class IO {

    private static Scanner scanner = new Scanner(System.in);

    public static String readln(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void print(String message) {
        System.out.print(message);
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}
