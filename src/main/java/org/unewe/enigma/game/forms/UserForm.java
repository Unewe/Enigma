package org.unewe.enigma.game.forms;

public class UserForm {
    private String username;
    private Object time;

    public UserForm(String username, Object time) {
        this.username = username;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }
}
