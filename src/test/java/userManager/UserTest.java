package userManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void makeUserOk() {
        int id=1;
        String name= "Pippo";
        String surname= "Topolino";
        String email = "pippo@gmail.com";
        String password = "password";
        User u;
        try {
            u=User.makeUser(id,name,surname,email,password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(name,u.getName());
        assertEquals(surname,u.getSurname());
        assertEquals(password,u.getPassword());
        assertEquals(email,u.getEmail());
        assertEquals(id,u.getId());
    }
    @Test
    void makeUserErrorPass() {
        int id=1;
        String name= "Pippo";
        String surname= "Topolino";
        String email = "pippo@gmail.com";
        String password = "";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
    @Test
    void makeUserErrorEmail() {
        int id=1;
        String name= "Pippo";
        String surname= "Topolino";
        String email = "";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
    @Test
    void makeUserErrorSurname() {
        int id=1;
        String name= "Pippo";
        String surname= "";
        String email = "pippo@gmail.com";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
    @Test
    void makeUserErrorName() {
        int id=1;
        String name= "";
        String surname= "Topolino";
        String email = "pippo@gmail.com";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));
    }
    @Test
    void makeUserErrorId() {
        int id=-1;
        String name= "Pippo";
        String surname= "Topolino";
        String email = "pippo@gmail.com";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
}