package test.common.util;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class Request implements Serializable {

    private String type;
    private String login;
    private String password;
    private String name;
    private ZonedDateTime data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Request(String type, String login, String password, String name, ZonedDateTime data) {
        this.type = type;
        this.login = login;
        this.password = password;
        this.name = name;
        this.data = data;
    }

    public Request(String type, String login, String password){
        this.type = type;
        this.login = login;
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}