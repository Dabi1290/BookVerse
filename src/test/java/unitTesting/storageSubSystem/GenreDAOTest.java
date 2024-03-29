package unitTesting.storageSubSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import storageSubSystem.GenreDAO;
import storageSubSystem.InvalidParameterException;
import testing.SQLScript;
import testing.RetrieveCredentials;
import userManager.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreDAOTest {

    private GenreDAO genreDAO;
    private DataSource ds;

    private Connection conn;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, InvalidParameterException {

        String[] crendentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", crendentials[0], crendentials[1]);

        executeSQLscript("src/test/db/init.sql");
        conn.setCatalog("BookVerseTest");

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);

        genreDAO = new GenreDAO(ds);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.close();
    }

    private void executeSQLscript(String scriptFilePath) throws SQLException {
        String[] sqlStatements = SQLScript.retrieveStatements(scriptFilePath);

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
    public void findAll_V1() throws SQLException{

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/GenreDAOTest/findAll_V1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        List<String> generi = genreDAO.findAll();

        assertEquals(0, generi.size());
    }

    @Test
    public void findAll_V2() throws SQLException{

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/GenreDAOTest/findAll_V2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        List<String> generi = genreDAO.findAll();

        assertEquals(1, generi.size());
    }

    @Test
    public void findAll_V3() throws SQLException{

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/GenreDAOTest/findAll_V3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        List<String> generi = genreDAO.findAll();

        assertEquals(2, generi.size());

    }
}