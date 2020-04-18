package de.mycrocast.dtalisty.messaging.request.parameter.user;

public class RegisterUserRequestParameter {

    private String name;
    private String password;

    public RegisterUserRequestParameter(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
