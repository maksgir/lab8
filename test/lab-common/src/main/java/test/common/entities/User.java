package test.common.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class User implements Serializable {
    private String name;
    private String login;
    private String password;
    private ZonedDateTime registrationDate;

    public User(String name, String login, String password, ZonedDateTime zdt) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.registrationDate = zdt;

    }

    public User(String login, String password){
        this.login = login;
        this.password = password;

    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getStringRegistrationDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        return dtf.format(registrationDate);
    }

    public void setRegistrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setName(String name) {
        this.name = name;
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
    @Override
    public String toString(){
        return (name == null ? "" : name)
                + (login == null ? "" : "\n" + login)
                + ((registrationDate == null) ? "" : "\n"
                + registrationDate);
    }
}
