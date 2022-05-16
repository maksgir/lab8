package test.client.util;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientCommandListener {
    private final Scanner sc;


    public ClientCommandListener(InputStream inputStream) {
        sc = new Scanner(inputStream);
    }

    public CommandToSend readCommand() {
        try {
            System.out.println("Введите команду: ");
            String[] splitInput = sc.nextLine().trim().split(" ");
            String commandName = splitInput[0].toLowerCase(Locale.ROOT);
            String[] commandsArgs = Arrays.copyOfRange(splitInput, 1, splitInput.length);
            return new CommandToSend(commandName, commandsArgs);
        } catch (NoSuchElementException e) {
            System.out.println("Неправильный символ введен, завершение работы...");
            System.exit(1);
            return null;
        }
    }
}
