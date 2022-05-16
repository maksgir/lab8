package test.common.entities;


import java.io.Serializable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Класс для хранения информации о маршруте
 */


public class Route implements Comparable<Route>, Serializable {
    /**
     * статичное поле для хранения id следующего создаваемого маршрута
     */


    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически


    private String name; //Поле не может быть null, Строка не может быть пустой


    private Coordinates coordinates; //Поле не может быть null


    private final ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически


    private Location from; //Поле не может быть null


    private Location to; //Поле не может быть null


    private long distance; //Значение поля должно быть больше 1

    public Route() {
        this.creationDate = ZonedDateTime.now(ZoneId.systemDefault());
    }

    public Route(Long id) {
        this.id = id;
        this.creationDate = ZonedDateTime.now(ZoneId.systemDefault());
    }


    public void setUpdatedId(Integer id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.equals("") || name == null) {
            throw new IllegalArgumentException("Название не может быть null или пустым");
        } else {
            this.name = name;
        }
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public static boolean validate(Route route) {
        if (route.getName() == null || route.getName().equals("") ||
                route.getCoordinates().getX() > route.getCoordinates().getMAX_X() ||
                route.getCoordinates().getX() == null || route.getCoordinates().getY() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Передайте координаты, а не null пж");
        } else {
            this.coordinates = coordinates;
        }
    }

    public String getCreationDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH ч mm мин");
        return String.format("Дата создания: %s", dtf.format(creationDate));
    }


    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Передайте локацию, а не null пж");
        } else {
            this.from = from;
        }

    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Передайте локацию, а не null пж");
        } else {
            this.to = to;
        }

    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;

    }

    @Override
    public int compareTo(Route o) {
        return Comparator.comparing(Route::getDistance).thenComparing(Route::getName)
                .thenComparing(Route::getCreationDate).compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("Route %s #%d %n Coordinates: %s %n Creation Date: %s %n From: %s %n To: %s %n Distance: %d",
                this.name, this.id, this.coordinates.toString(),
                getCreationDate(), this.from.toString(), this.to.toString(), this.distance);
    }


}
