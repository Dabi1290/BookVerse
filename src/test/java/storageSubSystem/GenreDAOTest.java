package storageSubSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import testing.ExtractStatementsFromScript;
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
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest?useUnicode=true;useJDBCCompliantTimezoneShift=true;useLegacyDatetimeCode=false;serverTimezone=UTC", "root", "Samuele$02");

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);

        genreDAO = new GenreDAO(ds);
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
    public void findAllV1() throws SQLException{

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/genreDAOTest/genreDAOV1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        List<String> generi = genreDAO.findAll();

        assertEquals(0, generi.size());

    }

    @Test
    public void findAllV2() throws SQLException{

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/genreDAOTest/genreDAOV2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        List<String> generi = genreDAO.findAll();

        assertEquals(1, generi.size());

    }



    @Test
    public void findAllV3() throws SQLException{

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/genreDAOTest/genreDAOV3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        List<String> generi = genreDAO.findAll();

        assertEquals(2, generi.size());

    }

}