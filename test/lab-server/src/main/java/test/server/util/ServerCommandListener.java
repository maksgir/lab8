package test.server.util;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerCommandListener {

    private final Scanner sc;

    public ServerCommandListener(Scanner sc) {
        this.sc = sc;
    }

    public String readCommand() {
        try {
            System.out.println("Введите команду: ");
            return sc.nextLine().trim().toLowerCase(Locale.ROOT);
        } catch (NoSuchElementException e) {
            System.out.println("Невалидный символ введен. Завершение работы.");
            System.exit(1);
            return null;
        }
    }
}