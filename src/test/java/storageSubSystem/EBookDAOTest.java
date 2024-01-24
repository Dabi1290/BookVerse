package storageSubSystem;

import ebookManager.EBook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import proposalManager.Proposal;
import testing.ExtractStatementsFromScript;
import testing.RetrieveCredentials;
import userManager.Author;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EBookDAOTest {

    private EBookDAO eBookDAO;

    private AuthorDAO authorDAO;
    private Connection conn;
    private DataSource ds;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, InvalidParameterException {
        String[] crendentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", crendentials[0], crendentials[1]);

        executeSQLscript("src/test/db/init.sql");
        conn.setCatalog("BookVerseTest");

        ds = Mockito.mock(DataSource.class);
        Answer<Connection> getConnection = new Answer<Connection>() {
            @Override
            public Connection answer(InvocationOnMock invocationOnMock) throws Throwable {
                String[] crendentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest", crendentials[0], crendentials[1]);
                conn.setCatalog("BookVerseTest");

                return conn;
            }
        };
        Mockito.when(ds.getConnection()).thenAnswer(getConnection);


        authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(1)).thenReturn(new Author(1));
        Mockito.when(authorDAO.findByID(2)).thenReturn(new Author(2));
        Mockito.when(authorDAO.findByID(3)).thenReturn(new Author(3));

        eBookDAO = new EBookDAO(ds, authorDAO);

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

        catch(SQLException ex) {
            System.out.println(ex.getSQLState());
            //System.out.println(ex.getErrorCode());
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    void newEbook_EV1_AV1_CP1() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/newEbook_EV1_AV1_CP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        EBook ebook = new EBook();
        ebook.setId(1);
        ebook.setEbookFile(new File("amicaGeniale.pdf"));
        ebook.setTitle("L'amica Geniale");

        HashSet<String> generi = new HashSet<>(Arrays.asList("Horror", "Fantasy"));
        ebook.setGenres(generi);

        ebook.setDescription("fiction RAI");
        ebook.setInCatalog(true);


        Author main = new Author(1);
        ebook.setMainAuthor(main);


        Author c1 = new Author(2);
        Author c2 = new Author(3);
        HashSet<Author> coautori = new HashSet<>(Arrays.asList(c1, c2));
        ebook.setCoAuthors(coautori);

        Proposal p = new Proposal();
        p.setId(1);
        p.setProposedBy(main);
        p.setCollaborators(coautori);

        ebook.setProposedThrough(p);

        assertEquals(1, eBookDAO.newEbook(ebook));


    }


    @Test
    void newEbook_EV1_AV1_CP2() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/newEbook_EV1_AV1_CP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        EBook ebook = new EBook();
        ebook.setId(1);
        ebook.setEbookFile(new File("amicaGeniale.pdf"));
        ebook.setTitle("L'amica Geniale");

        HashSet<String> generi = new HashSet<>(Arrays.asList("Horror", "Fantasy"));
        ebook.setGenres(generi);

        ebook.setDescription("fiction RAI");
        ebook.setInCatalog(true);


        Author main = new Author(1);
        ebook.setMainAuthor(main);


        Author c1 = new Author(2);
        Author c2 = new Author(10);
        HashSet<Author> coautori = new HashSet<>(Arrays.asList(c1, c2));
        ebook.setCoAuthors(coautori);

        Proposal p = new Proposal();
        p.setId(1);
        p.setProposedBy(main);
        p.setCollaborators(coautori);

        ebook.setProposedThrough(p);

        assertThrows(InvalidParameterException.class, ()->eBookDAO.newEbook(ebook));


    }

    @Test
    void newEbook_EV1_AV2_CP2() throws Exception {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/newEbook_EV1_AV1_CP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        EBook ebook = new EBook();
        ebook.setId(1);
        ebook.setEbookFile(new File("amicaGeniale.pdf"));
        ebook.setTitle("L'amica Geniale");

        HashSet<String> generi = new HashSet<>(Arrays.asList("Horror", "Fantasy"));
        ebook.setGenres(generi);

        ebook.setDescription("fiction RAI");
        ebook.setInCatalog(true);


        Author main = new Author(-1);
        ebook.setMainAuthor(main);


        Author c1 = new Author(2);
        Author c2 = new Author(10);
        HashSet<Author> coautori = new HashSet<>(Arrays.asList(c1, c2));
        ebook.setCoAuthors(coautori);

        Proposal p = new Proposal();
        p.setId(1);
        p.setProposedBy(main);
        p.setCollaborators(coautori);

        ebook.setProposedThrough(p);

        assertThrows(InvalidParameterException.class, ()->eBookDAO.newEbook(ebook));


    }

    @Test
    void findByCoWriter_IA1_ADB1_NC1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB1_NC1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByCoWriter(2);

        assertEquals(0, ebooks.size());

    }


    @Test
    void findByCoWriter_IA1_ADB1_NC2() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB1_NC2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByCoWriter(2);

        assertEquals(1, ebooks.size());

        HashSet<EBook> oracolo = new HashSet<>();
        EBook e = new EBook();
        e.setId(1);
        oracolo.add(e);

        assertTrue(ebooks.containsAll(oracolo) && oracolo.containsAll(ebooks));

    }

    @Test
    void findByCoWriter_IA1_ADB1_NC3() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB1_NC3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByCoWriter(2);

        assertEquals(2, ebooks.size());

        //creo oracolo
        HashSet<EBook> oracolo = new HashSet<>();
        EBook e = new EBook();
        e.setId(1);
        oracolo.add(e);
        EBook e1 = new EBook();
        e1.setId(2);
        oracolo.add(e1);

        assertTrue(ebooks.containsAll(oracolo) && oracolo.containsAll(ebooks));

    }

    @Test
    void findByCoWriter_IA1_ADB2_NC1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB2_NC1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        assertThrows(InvalidParameterException.class, ()->eBookDAO.findByCoWriter(10));

    }

    @Test
    void findByCoWriter_IA2_ADB2_NC1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB2_NC1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        assertThrows(InvalidParameterException.class, ()->eBookDAO.findByCoWriter(-1));

    }


    @Test
    void findByMainWriter_IA1_ADB1_NC1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByMainWriter(1);

        assertEquals(0, ebooks.size());

    }

    @Test
    void findByMainWriter_IA1_ADB1_NC2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByMainWriter(1);

        assertEquals(1, ebooks.size());

        //creo oracolo
        HashSet<EBook> oracolo = new HashSet<>();
        EBook e = new EBook();
        e.setId(1);
        oracolo.add(e);

        assertTrue(ebooks.containsAll(oracolo) && oracolo.containsAll(ebooks));
    }


    @Test
    void findByMainWriter_IA1_ADB1_NC3() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByMainWriter(1);

        assertEquals(2, ebooks.size());

        //creo oracolo
        HashSet<EBook> oracolo = new HashSet<>();
        EBook e = new EBook();
        e.setId(1);
        oracolo.add(e);
        EBook e1 = new EBook();
        e1.setId(2);
        oracolo.add(e1);

        assertTrue(ebooks.containsAll(oracolo) && oracolo.containsAll(ebooks));
    }

    @Test
    void findByMainWriter_IA1_ADB2_NC1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        assertThrows(InvalidParameterException.class, ()->eBookDAO.findByMainWriter(10));
    }

    @Test
    void findByMainWriter_IA2_ADB2_NC1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        assertThrows(InvalidParameterException.class, ()->eBookDAO.findByMainWriter(-1));
    }


}