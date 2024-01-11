package userManager;

import java.util.Collection;

public class User {
    private int id;
    private String email;
    private String password;
    private String surname;
    private String name;
    private String currentRole;
    private Author roleAuthor;
    private Validator roleValidator;
    private Collection<String> roles;

    public User() {

    }

    public static User makeUser(String name, String surname, String email, String password) {
        User user = new User();

        user.name = name;
        user.surname = surname;
        user.email = email;
        user.password = password;

        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }
}
