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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDAOTest {
    private DataSource dataSource;
    private String email;
    private String role;
    private String password;
    private Connection conn;
    private DataSource ds;

    @BeforeEach
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest?useUnicode=true;useJDBCCompliantTimezoneShift=true;useLegacyDatetimeCode=false;serverTimezone=UTC", "root", "%(dJ*6!tuB4PA^Fp");

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    public void login_VE1_VP1_VR1_VD1() throws Exception {

        String scriptFilePath = "src/test/db/login_VE1_VP1_VR1_VD1.sql";

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



        UserDAO userDAO = new UserDAO(ds);

        User user = userDAO.login("email", "password", "Author");

        boolean equals = user.getPassword().equals("password") && user.getEmail().equals("email") && user.getCurrentRole().equals("Author") && user.getId() == 1
                && user.getName().equals("antonio") && user.getSurname().equals("ambrosio");
        assertTrue(equals);
    }

    @Test
    public void login_VE1_VP1_VR1_VD2() {
        String scriptFilePath = "src/test/db/login_VE1_VP1_VR1_VD1.sql";
    }

    @Test
    public void login_VE1_VP1_VR2_VD1() {

    }

    @Test
    public void login_VE1_VP2_VR1_VD1() {

    }

    @Test
    public void login_VE2_VP1_VR1_VD1() {

    }
}
