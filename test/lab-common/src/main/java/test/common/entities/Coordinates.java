package test.common.entities;

import java.io.Serializable;

/**
 * Исходный класс для хранения информации о координатах маршрута
 */
public class Coordinates implements Serializable {
    public static final Integer MAX_X = 981;
    public static final Integer MAX_Y = 1000;


    private Integer x; //Максимальное значение поля: 981, Поле не может быть null


    private Long y; //Поле не может быть null

    public Coordinates(){}

    public Coordinates(int x, long y){
        this.x = x;
        this.y = y;
    }


    public void setX(Integer x) {
        this.x = x;

    }

    public int getMAX_X() {
        return this.MAX_X;
    }

    public Integer getX() {
        return this.x;
    }

    public void setY(Long y) {
        this.y = y;

    }

    public Long getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }


}
