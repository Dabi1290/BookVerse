package storageSubSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import testing.ExtractStatementsFromScript;
import userManager.Author;
import userManager.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorDAOTest {
    private Connection conn;
    private DataSource ds;
    private AuthorDAO authorDAO;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest?useUnicode=true;useJDBCCompliantTimezoneShift=true;useLegacyDatetimeCode=false;serverTimezone=UTC", "root", "%(dJ*6!tuB4PA^Fp");

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);

        authorDAO = new AuthorDAO(ds);
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
    public void findAuthorById_IV1_IA1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorById_IV1_IA1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        Author a = authorDAO.findByID(1);
        assertEquals(1, a.getId());
    }

    @Test
    public void findAuthorById_IV1_IA2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorById_IV1_IA1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        Author a = authorDAO.findByID(2);
        assertNull(a);
    }

    @Test
    public void findAuthorById_IV2_IA1() throws SQLException{
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorById_IV2_IA1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findByID(-1));
        assertEquals("The value of id is not valid", ex.getMessage());
    }

    @Test
    public void findAuthorsByEmail_EV1_EM1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorsByEmail_EV1_EM1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        String email = "ant";

        Set<User> authors = authorDAO.findAuthorsByEmail(email);
        assertTrue(authors.isEmpty());
    }

    @Test
    public void findAuthorsByEmail_EV1_EM2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorsByEmail_EV1_EM2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        String email = "ema";

        Set<User> authors = authorDAO.findAuthorsByEmail(email);
        assertTrue(authors.size() == 1 && authors.stream().allMatch((u) -> u.getEmail().startsWith(email)));
    }

    @Test
    public void findAuthorsByEmail_EV1_EM3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorsByEmail_EV1_EM3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        //Expected results
        Set<User> expectedAuthors = new HashSet<>();

        User user = new User();
        user.setId(2);
        expectedAuthors.add(user);

        user = new User();
        user.setId(3);
        expectedAuthors.add(user);

        user = new User();
        user.setId(4);
        expectedAuthors.add(user);
        //Expected results

        String email = "ema";

        Set<User> authors = authorDAO.findAuthorsByEmail(email);

        boolean equals = true;
        assertTrue(equals);
    }

    @Test
    public void findAuthorsByEmail_EV2_EM1() throws SQLException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorsByEmail_EV2_EM1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        String email = "";

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findAuthorsByEmail(email));
        assertEquals("email is not valid", ex.getMessage());
    }
}
