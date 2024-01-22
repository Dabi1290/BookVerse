package storageSubSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import userManager.User;
import testing.ExtractStatementsFromScript;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private String email;
    private String role;
    private String password;
    private Connection conn;
    private DataSource ds;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest?useUnicode=true;useJDBCCompliantTimezoneShift=true;useLegacyDatetimeCode=false;serverTimezone=UTC", "root", "%(dJ*6!tuB4PA^Fp");

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);

        userDAO = new UserDAO(ds);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.close();
    }

    private void executeSQLscript(String scriptFilePath) throws SQLException {
        String[] sqlStatements = ExtractStatementsFromScript.retrieveStatements(scriptFilePath);

        // Create a Statement
        try (Statement statement = conn.createStatement()) {
            // Execute each SQL statement
            for (String sql : sqlStatements) {
                // Skip empty statements
                if (!sql.trim().isEmpty()) {
                    statement.execute(sql.trim());
                }
            }
        }
    }

    @Test
    public void login_VE1_VP1_VR1_VD1() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/UserDAOTest/createDbForTest.sql");
        String scriptFilePath = "src/test/db/UserDAOTest/login_VE1_VP1_VR1_VD1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        User user = userDAO.login("email", "password", "Author");

        boolean equals = user.getPassword().equals("password") && user.getEmail().equals("email") && user.getCurrentRole().equals("Author") && user.getId() == 1
                && user.getName().equals("antonio") && user.getSurname().equals("ambrosio");

        assertTrue(equals);
    }

    @Test
    public void login_VE1_VP1_VR1_VD2() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/UserDAOTest/createDbForTest.sql");
        String scriptFilePath = "src/test/db/UserDAOTest/login_VE1_VP1_VR1_VD2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        User user = userDAO.login("email@gmail.com", "asdasdsdad", "Author");
        assertNull(user);
    }

    @Test
    public void login_VE1_VP1_VR2_VD2() throws SQLException {

        //Prepare database
        executeSQLscript("src/test/db/UserDAOTest/createDbForTest.sql");
        String scriptFilePath = "src/test/db/UserDAOTest/login_VE1_VP1_VR2_VD2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Exception e = assertThrows(Exception.class, () -> userDAO.login("email@gmail.com", "asdasdsdad", "Admin"));
        assertEquals(e.getMessage(), "Not supported value for role");
    }

    @Test
    public void login_VE1_VP2_VR1_VD2() throws SQLException {

        //Prepare database
        executeSQLscript("src/test/db/UserDAOTest/createDbForTest.sql");
        String scriptFilePath = "src/test/db/UserDAOTest/login_VE1_VP2_VR1_VD2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Exception e = assertThrows(Exception.class, () -> userDAO.login("email@gmail.com", "", "Admin"));
        assertEquals(e.getMessage(), "Not supported value for passowrd");
    }

    @Test
    public void login_VE2_VP1_VR1_VD2() throws SQLException {

        //Prepare database
        executeSQLscript("src/test/db/UserDAOTest/createDbForTest.sql");
        String scriptFilePath = "src/test/db/UserDAOTest/login_VE2_VP1_VR1_VD2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Exception e = assertThrows(Exception.class, () -> userDAO.login(null, "asdasdsdad", "Admin"));
        assertEquals(e.getMessage(), "Not supported value for email");
    }
}
