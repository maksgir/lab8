package test.client;

import test.client.util.*;
import test.common.entities.User;
import test.common.exceptions.WrongAmountOfArgsException;
import test.common.util.Request;
import test.common.util.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientWorker {
    private final Scanner scanner = new Scanner(System.in);
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final int maxPort = 65535;
    private ClientSocketWorker clientSocketWorker;
    private final ClientCommandListener commandListener = new ClientCommandListener(System.in);
    private final RequestCreator requestCreator = new RequestCreator();
    private boolean statusOfCommandListening = true;
    private String login;
    private String password;
    private String name;
    private boolean isAuthorised;


    public void startClientWorker() {
        System.out.println("Добро пожаловать в программу!");

        inputAddress();
        inputPort();

        welcome();

        System.out.println(isAuthorised);

        while (statusOfCommandListening) {

            CommandToSend command = commandListener.readCommand();
            if (command != null) {
                if ("exit".equals(command.getCommandName().toLowerCase(Locale.ROOT))) {
                    System.out.println("Завершение работы клиента\nДо встречи, " + this.login);
                    toggleStatus();
                } else if (AvailableCommands.SCRIPT_ARGUMENT_COMMAND.equals(command.getCommandName())) {
                    executeScript(command.getCommandArgs());
                } else {
                    Request request = requestCreator.createRequestOfCommand(command);
                    request.setUser(new User(login, password));
                    if (sendRequest(request)) {
                        receiveResponse();
                    }
                }
            }


        }

    }


    public void toggleStatus() {
        statusOfCommandListening = !statusOfCommandListening;
    }

    private void welcome() {
        System.out.println("Вы новый пользователь?(y/n)");
        try {
            String answer = reader.readLine().trim().toLowerCase(Locale.ROOT);
            if (answer.equals("y")) {
                registration();
            } else if (answer.equals("n")) {
                loggingIn();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loggingIn() {
        System.out.println("Авторизация пользователя...");
        login = setLogin();
        password = setSimplePassword();


        Request request = new Request("login", new User(login, password));
        if (sendRequest(request)) {
            boolean regSuccessful = receiveResponse();
            if (!regSuccessful) {
                welcome();
            } else {
                isAuthorised = true;
            }
        }


    }

    private void registration() {
        System.out.println("Регистрация нового пользователя...");
        name = setName();
        login = setLogin();
        password = setPassword();
        ZonedDateTime zdt = ZonedDateTime.now();


        Request request = new Request("registration", new User(name, login, password, zdt));
        if (sendRequest(request)) {
            boolean regSuccessful = receiveResponse();
            if (!regSuccessful) {
                loggingIn();
            } else {
                isAuthorised = true;
            }
        }


    }

    private String setName() {
        String newName = null;
        boolean ready = false;

        do {
            System.out.println("Введите ваше имя:");
            try {
                newName = reader.readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (newName != null && !newName.equals("")) {
                ready = true;
            } else {
                System.out.println("Имя не может быть пустой строкой. Попробуйте еще раз...");
            }
        } while (!ready);


        return newName;
    }

    private String setLogin() {
        String newLog = null;
        boolean ready = false;

        do {
            System.out.println("Введите логин:");
            try {
                newLog = reader.readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (newLog != null && !newLog.equals("")) {
                ready = true;
            } else {
                System.out.println("Логин не может быть пустой строкой. Попробуйте еще раз...");
            }
        } while (!ready);
        return newLog;
    }


    private String setPassword() {
        String pass1 = null;
        boolean ready1 = false;

        do {
            System.out.println("Введите пароль:");
            try {
                pass1 = reader.readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (pass1 != null && !pass1.equals("")) {
                ready1 = true;
            } else {
                System.out.println("Пароль не может быть пустой строкой. Попробуйте еще раз...");
            }
        } while (!ready1);

        String pass2 = null;
        boolean ready2 = false;

        do {
            System.out.println("Введите пароль еще раз:");
            try {
                pass2 = reader.readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (pass2 != null && !pass2.equals("")) {
                ready2 = true;
            } else {
                System.out.println("Пароль не может быть пустой строкой. Попробуйте еще раз...");
            }
        } while (!ready2);

        if (!pass1.equals(pass2)) {
            System.out.println("Пароли не совпадают...Попробуйте еще раз.");
            pass2 = setPassword();
        }
        return pass2;
    }

    private String setSimplePassword() {

        String newPass = null;
        boolean ready = false;

        do {
            System.out.println("Введите пароль");
            try {
                newPass = reader.readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (newPass != null && !newPass.equals("")) {
                ready = true;
            } else {
                System.out.println("Пароль не может быть пустой строкой. Попробуйте еще раз...");
            }
        } while (!ready);
        return newPass;

    }

    private boolean sendRequest(Request request) {
        if (request != null) {
            try {
                clientSocketWorker.sendRequest(request);
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred while serializing the request, try again");
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean receiveResponse() {
        try {
            Response response = clientSocketWorker.receiveResponse();
            System.out.println(response);
            return response.isSuccessful();
        } catch (SocketTimeoutException e) {
            System.out.println("Время ожидания отклика от сервера превышено, попробуйте позже");
            return false;
        } catch (IOException e) {
            System.out.println("Ошибка получения ответа от сервера");
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Ответ от сервера пришел поврежденный");
            return false;
        }

    }

    private void inputAddress() {
        System.out.println("Вы хотите использовать дефолтный адрес сервера? [y/n]");
        try {
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("y".equals(s)) {
                clientSocketWorker = new ClientSocketWorker();
            } else if ("n".equals(s)) {
                System.out.println("Пожалуйста, введите IP адрес сервера");
                String address = scanner.nextLine();
                clientSocketWorker = new ClientSocketWorker();
                clientSocketWorker.setAddress(address);
            } else {
                System.out.println("Вы ввели невалидный символ, попробуйте еще раз");
                inputAddress();
            }
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный адрес");
            inputAddress();
        } catch (SocketException e) {
            System.out.println("Возникли ошибки при создании порта, попробуйте снова");
            inputAddress();
        } catch (NoSuchElementException e) {
            System.out.println("Введен неверный символ, отключение от сервера");
            System.exit(1);
        }
    }

    private void inputPort() {
        System.out.println("Вы хотите использовать дефолтный порт? [y/n]");
        try {
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                System.out.println("Введите номер порта (1-65535)");
                String port = scanner.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= maxPort) {
                        clientSocketWorker.setPort(portInt);
                    } else {
                        System.out.println("Номер порта не подходит под критерии,попробуйте еще раз");
                        inputPort();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка ввода числа, попробуйте еще раз");
                    inputPort();
                }
            } else if (!"y".equals(s)) {
                System.out.println("Вы введи невалидный символ, попробуйте еще раз");
                inputPort();
            }
        } catch (NoSuchElementException e) {
            System.out.println("Введен неверный символ, отключение от сервера");
            System.exit(1);
        }
    }

    private void executeScript(String[] args) {
        try {
            CommandValidators.validateAmountOfArgs(args, 1);
            ScriptReader reader = new ScriptReader();

            if (ScriptsHistory.getHistoryOfScripts().contains(args[0])) {
                System.out.println("Possible looping, change your script");
            } else {
                reader.readCommandsFromFile(args[0]);
                ScriptsHistory.addToScriptHistory(args[0]);
                ArrayList<CommandToSend> commands = reader.getCommandsFromFile();
                for (CommandToSend command : commands) {
                    System.out.println("Executing... " + command.getCommandName());
                    if ("execute_script".equals(command.getCommandName())) {
                        executeScript(command.getCommandArgs());
                    } else {
                        Request request = requestCreator.createRequestOfCommand(command);
                        if (sendRequest(request)) {
                            receiveResponse();
                            System.out.println(command.getCommandName());
                        }
                    }
                }
            }
        } catch (WrongAmountOfArgsException | IOException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("An invalid character has been entered, forced shutdown!");
            System.exit(1);
        }
    }

}
