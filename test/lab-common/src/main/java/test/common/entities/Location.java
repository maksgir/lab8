package test.common.entities;

import java.io.Serializable;

public class Location implements Serializable {

    /**
     * поля для хранения координат
     */

    private Double x; //Поле не может быть null

    private long y;
    /**
     * поле для хранения названия локации
     */

    private String name; //Поле не может быть null


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return name + " x: " + x + " y: " + y;
    }
}