package userManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void makeUser_VI1_VN1_VS1_VE1_VP1() {
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
    void makeUser_VI1_VN1_VS1_VE1_VP2() {
        int id=1;
        String name= "Pippo";
        String surname= "Topolino";
        String email = "pippo@gmail.com";
        String password = "";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
    @Test
    void makeUser_VI1_VN1_VS1_VE2_VP1() {
        int id=1;
        String name= "Pippo";
        String surname= "Topolino";
        String email = "";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
    @Test
    void makeUser_VI1_VN1_VS2_VE1_VP1() {
        int id=1;
        String name= "Pippo";
        String surname= "";
        String email = "pippo@gmail.com";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
    @Test
    void makeUser_VI1_VN2_VS1_VE1_VP1() {
        int id=1;
        String name= "";
        String surname= "Topolino";
        String email = "pippo@gmail.com";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));
    }
    @Test
    void makeUser_VI2_VN1_VS1_VE1_VP1() {
        int id=-1;
        String name= "Pippo";
        String surname= "Topolino";
        String email = "pippo@gmail.com";
        String password = "password";
        assertThrows(Exception.class,()->User.makeUser(id,name,surname,email,password));

    }
}