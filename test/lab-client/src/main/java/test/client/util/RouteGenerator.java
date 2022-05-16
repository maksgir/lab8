package test.client.util;

import test.common.entities.Coordinates;
import test.common.entities.Location;
import test.common.entities.Route;

import java.util.Scanner;

public class RouteGenerator {
    private final int maxNameLength = 100;


    private final Route generatedRoute;

    private final Coordinates coordinates = new Coordinates();

    private final Location from = new Location();

    private final Location to = new Location();

    private final Scanner sc = new Scanner(System.in);

    public RouteGenerator() {
        generatedRoute = new Route();
        generatedRoute.setCoordinates(coordinates);
        generatedRoute.setTo(to);
        generatedRoute.setFrom(from);
        generatedRoute.setCreationDate();
    }

    public Route getGeneratedRoute() {
        return generatedRoute;
    }

    private void getName() {
        String name = CommandValidators.validateStringInput("Введите название маршрута (максимальная длина + "
                        + maxNameLength
                        + " символов)",
                false,
                sc,
                maxNameLength);
        generatedRoute.setName(name);
    }

    private void getXCoordinate() {
        int x = CommandValidators.validateInput(arg -> ((int) arg) <= Coordinates.MAX_X,
                "Введите Х координату маршрута (значение не должно быть больше чем " + Coordinates.MAX_X + ")",
                "Ошибка обработки числа, повторите ввод",
                "Х координата должна быть не больше чем " + Coordinates.MAX_X + ", повторите ввод",
                Integer::parseInt,
                false,
                sc);
        generatedRoute.getCoordinates().setX(x);
    }

    private void getYCoordinate() {
        long y = CommandValidators.validateInput(arg -> ((long) arg) <= Coordinates.MAX_Y,
                "Введите У координату маршрута (значение не должно быть больше чем " + Coordinates.MAX_Y + ")",
                "Ошибка обработки числа, повторите ввод",
                "У координата должна быть не больше чем " + Coordinates.MAX_Y + ", повторите ввод",
                Long::parseLong,
                false,
                sc);
        generatedRoute.getCoordinates().setY(y);
    }

    private void getDistance() {
        long distance = CommandValidators.validateInput(arg -> ((long) arg) > 1,
                "Введите длину маршрута",
                "Ошибка обработки числа, повторите ввод",
                "Длина маршрута должна быть больше 1, повторите ввод",
                Long::parseLong,
                false,
                sc);
        generatedRoute.setDistance(distance);
    }

    private String getLocationName() {
        return CommandValidators.validateStringInput("Введите название локации (максимальная длина + "
                        + maxNameLength
                        + " символов)",
                false,
                sc,
                maxNameLength);
    }

    private Double getLocationX() {
        return CommandValidators.validateInput(arg -> ((double) arg) > 0,
                "Введите Х координату локации (больше 0)",
                "Ошибка обработки числа, повторите ввод",
                "Х координата должна быть больше 0",
                Double::parseDouble,
                false,
                sc);
    }

    private Long getLocationY() {
        return CommandValidators.validateInput(arg -> ((long) arg) > 0,
                "Введите У координату локации (больше 0)",
                "Ошибка обработки числа, повторите ввод",
                "У координата должна быть больше 0",
                Long::parseLong,
                false,
                sc);
    }


    private void getLocationTo() {
        generatedRoute.getTo().setName(getLocationName());
        generatedRoute.getTo().setX(getLocationX());
        generatedRoute.getTo().setY(getLocationY());
    }

    private void getLocationFrom() {
        generatedRoute.getFrom().setName(getLocationName());
        generatedRoute.getFrom().setX(getLocationX());
        generatedRoute.getFrom().setY(getLocationY());
    }


    public void setVariables() {
        getName();
        getXCoordinate();
        getYCoordinate();
        getDistance();
        System.out.println("Собираем данные о локации отправления");
        getLocationFrom();
        System.out.println("Собираем данные о локации следования");
        getLocationTo();

    }


}
