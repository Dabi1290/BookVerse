package userManager;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class User {
    private int id;
    private String email;
    private String password;
    private String surname;
    private String name;
    private String currentRole;
    private boolean alreadyLoadedRoleAuthor;
    private Author roleAuthor;
    private boolean alreadyLoadedRoleValidator;
    private Validator roleValidator;

    public User() {
        this.alreadyLoadedRoleAuthor = false;
        this.alreadyLoadedRoleValidator = false;
    }

    public static User makeUser(int id, String name, String surname, String email, String password) throws Exception {

        //Check parameters
        if(id <= 0)
            throw new Exception("value not valid for id");

        if(name == null || name.isEmpty())
            throw new Exception("value not valid for name");

        if(surname == null || surname.isEmpty())
            throw new Exception("value not valid for surname");

        if(email == null || email.isEmpty())
            throw new Exception("value not valid for email");

        if(password == null || password.isEmpty())
            throw new Exception("value not valid for password");
        //Check parameters

        User user = new User();

        user.id = id;
        user.name = name;
        user.surname = surname;
        user.email = email;
        user.password = password;

        return user;
    }

    public static User makeUser(int id, String name, String surname, String email) throws Exception {

        //Check parameters
        if(id <= 0)
            throw new Exception("value not valid for id");

        if(name == null || name.isEmpty())
            throw new Exception("value not valid for name");

        if(surname == null || surname.isEmpty())
            throw new Exception("value not valid for surname");

        if(email == null || email.isEmpty())
            throw new Exception("value not valid for email");
        //Check parameters

        User user = new User();

        user.id = id;
        user.name = name;
        user.surname = surname;
        user.email = email;

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

    public boolean isAlreadyLoadedRoleAuthor() {
        return alreadyLoadedRoleAuthor;
    }

    public void setAlreadyLoadedRoleAuthor(boolean alreadyLoadedRoleAuthor) {
        this.alreadyLoadedRoleAuthor = alreadyLoadedRoleAuthor;
    }

    public Author getRoleAuthor() {
        return roleAuthor;
    }

    public void setRoleAuthor(Author roleAuthor) {
        this.roleAuthor = roleAuthor;
    }

    public boolean isAlreadyLoadedRoleValidator() {
        return alreadyLoadedRoleValidator;
    }

    public void setAlreadyLoadedRoleValidator(boolean alreadyLoadedRoleValidator) {
        this.alreadyLoadedRoleValidator = alreadyLoadedRoleValidator;
    }

    public Validator getRoleValidator() {
        return roleValidator;
    }

    public void setRoleValidator(Validator roleValidator) {
        this.roleValidator = roleValidator;
    }
}
