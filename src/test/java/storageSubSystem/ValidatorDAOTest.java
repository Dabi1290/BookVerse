package storageSubSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import testing.SQLScript;
import testing.RetrieveCredentials;
import userManager.Author;
import userManager.Validator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorDAOTest {

    private Connection conn;
    private DataSource ds;
    private ValidatorDAO validatorDAO;

    private AuthorDAO authorDAO;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, InvalidParameterException {

        String[] crendentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", crendentials[0], crendentials[1]);

        executeSQLscript("src/test/db/init.sql");
        conn.setCatalog("BookVerseTest");

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);



        authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(2)).thenReturn(new Author(2));
        Mockito.when(authorDAO.findByID(3)).thenReturn(new Author(3));
        Mockito.when(authorDAO.findByID(4)).thenReturn(new Author(4));

        validatorDAO = new ValidatorDAO(ds, authorDAO);
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
    public void findFreeValidatorM1_C1_AP1() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ValidatorDAOTest/findFreeValidatorM1_C1_AP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Author a = new Author(2);
        Author b = new Author(3);
        Author c = new Author(4);

        HashSet<Author> set = new HashSet<>();
        set.add(b);
        set.add(c);

        Validator v = validatorDAO.findFreeValidator(a, set);

        assertEquals(1, v.getId());

    }

    @Test
    public void findFreeValidatorM1_C2_AP2() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ValidatorDAOTest/findFreeValidatorM1_C2_AP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Author a = new Author(2);
        Author b = new Author(3);
        Author c = new Author(10);

        HashSet<Author> set = new HashSet<>();
        set.add(b);
        set.add(c);

        assertThrows(InvalidParameterException.class, ()->validatorDAO.findFreeValidator(a, set));

    }

    @Test
    public void findFreeValidatorM2_C1_AP2() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ValidatorDAOTest/findFreeValidatorM2_C2_AP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Author a = new Author(10);
        Author b = new Author(3);
        Author c = new Author(4);

        HashSet<Author> set = new HashSet<>();
        set.add(b);
        set.add(c);

        assertThrows(InvalidParameterException.class, ()->validatorDAO.findFreeValidator(a, set));

    }

    @Test
    public void findValidatorByIdV1_P1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ValidatorDAOTest/findValidatorByIdV1_P1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Validator v = validatorDAO.findValidatorById(1);

        assertEquals(1, v.getId());

    }

    @Test
    public void findValidatorByIdV1_P2() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ValidatorDAOTest/findValidatorByIdV1_P1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        assertNull(validatorDAO.findValidatorById(10));

    }

    @Test
    public void findValidatorByIdV2_P2() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ValidatorDAOTest/findValidatorByIdV1_P1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        assertThrows(InvalidParameterException.class, ()->validatorDAO.findValidatorById(-1));


    }

}