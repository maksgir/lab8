package test.common.entities;


import java.io.Serializable;
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


    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически


    private String name; //Поле не может быть null, Строка не может быть пустой


    private Coordinates coordinates; //Поле не может быть null


    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически


    private Location from; //Поле не может быть null


    private Location to; //Поле не может быть null


    private long distance; //Значение поля должно быть больше 1

    private String owner;


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Передайте координаты, а не null пж");
        } else {
            this.coordinates = coordinates;
        }
    }

    public String getStringOfCreationDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        return dtf.format(creationDate);
    }

    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(ZonedDateTime zdt) {
        this.creationDate = zdt;
    }

    public void setCreationDate() {
        this.creationDate = ZonedDateTime.now();
    }


    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {

        this.to = to;


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
                .thenComparing(Route::getStringOfCreationDate).compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("Route %s #%d владельца: %s %n Координаты: %s %n Дата создания: %s %n Из: %s %n В: %s %n Дистанция: %d",
                this.name, this.id, this.owner, this.coordinates.toString(),
                getStringOfCreationDate(), this.from.toString(), this.to.toString(), this.distance);
    }


}
