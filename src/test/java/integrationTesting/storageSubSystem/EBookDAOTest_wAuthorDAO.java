package integrationTesting.storageSubSystem;

import ebookManager.EBook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import proposalManager.Proposal;
import storageSubSystem.AuthorDAO;
import storageSubSystem.EBookDAO;
import storageSubSystem.InvalidParameterException;
import testing.RetrieveCredentials;
import userManager.Author;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testing.SQLScript.executeSQLScript;

public class EBookDAOTest_wAuthorDAO {

    private Connection conn;
    private DataSource ds;
    private EBookDAO eBookDAO;
    private AuthorDAO authorDAO;

    @BeforeEach
    public void setUp() throws ClassNotFoundException, SQLException, InvalidParameterException {

        String[] crendentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", crendentials[0], crendentials[1]);

        executeSQLScript("src/test/db/init.sql", conn);
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

    @AfterEach
    public void tearDown() throws SQLException {
        conn.close();
    }



    private Connection newConnection() throws SQLException {
        String[] credentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest", credentials[0], credentials[1]);
        connection.setCatalog("BookVerseTest");

        return connection;
    }



    @Test
    void newEbook_EV1_AV1_CP1() throws Exception {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/newEbook_EV1_AV1_CP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        int expectedEbookId = 1;

        EBook ebook = new EBook();
        ebook.setId(expectedEbookId);
        ebook.setEbookFile(new File("amicaGeniale.pdf"));
        ebook.setTitle("L'amica Geniale");

        Set<String> generi = new HashSet<>(Arrays.asList("Horror", "Fantasy"));
        ebook.setGenres(generi);

        ebook.setDescription("fiction RAI");
        ebook.setInCatalog(true);


        Author main = new Author(1);
        ebook.setMainAuthor(main);


        Author c1 = new Author(2);
        Author c2 = new Author(3);
        HashSet<Author> coautori = new HashSet<>(Arrays.asList(c1, c2));
        ebook.setCoAuthors(coautori);

        int proposalId = 1;
        Proposal p = new Proposal();
        p.setId(proposalId);
        p.setProposedBy(main);
        p.setCollaborators(coautori);

        ebook.setProposedThrough(p);



        assertEquals(expectedEbookId, eBookDAO.newEbook(ebook));


        //Check if the method saved correctly on database information
        String query = "SELECT * FROM EBook as b WHERE b.id = ?";

        Connection c = newConnection();



        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, expectedEbookId);

        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(proposalId, rs.getInt("proposalId_fk"));
        assertEquals(ebook.getTitle(), rs.getString("title"));
        assertEquals(ebook.getPrice(), rs.getInt("price"));
        assertEquals(ebook.getDescription(), rs.getString("description"));
        assertEquals(ebook.getProposedThrough().getId(), rs.getInt("mainAuthorId_fk"));



        query = "SELECT * FROM EBookGenre as EG WHERE EG.ebookId_fk = ? AND EG.genreId_fk = ? ";

        ps.clearParameters();
        ps = c.prepareStatement(query);
        ps.setInt(1, expectedEbookId);
        ps.setString(2, "Horror");
        rs = ps.executeQuery();

        assertTrue(rs.next());

        ps.clearParameters();
        ps = c.prepareStatement(query);
        ps.setInt(1, expectedEbookId);
        ps.setString(2, "Fantasy");
        rs = ps.executeQuery();

        assertTrue(rs.next());



        query = "SELECT * FROM EBookAuthor as EA WHERE EA.ebookId_fk = ? AND EA.authorId_fk = ?";

        for(Author coAuthor : coautori) {

            ps.clearParameters();
            ps = c.prepareStatement(query);
            ps.setInt(1, expectedEbookId);
            ps.setInt(2, coAuthor.getId());
            rs = ps.executeQuery();

            assertTrue(rs.next());
        }

        c.close();
        //Check if the method saved correctly on database information
    }


    @Test
    void newEbook_EV1_AV1_CP2() throws Exception {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/newEbook_EV1_AV1_CP2.sql";
        executeSQLScript(scriptFilePath, conn);
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

        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->eBookDAO.newEbook(ebook));
        assertEquals("at least one coAuthors does'nt exist on database", e.getMessage());
    }

    @Test
    void newEbook_EV1_AV2_CP2() throws Exception {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/newEbook_EV1_AV1_CP2.sql";
        executeSQLScript(scriptFilePath, conn);
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

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, ()->eBookDAO.newEbook(ebook));
        assertEquals("this mainAuthor is not valid", ex.getMessage());
    }




    
    @Test
    void findByCoWriter_IA1_ADB1_NC1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB1_NC1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByCoWriter(2);

        assertTrue(ebooks.isEmpty());
    }


    @Test
    void findByCoWriter_IA1_ADB1_NC2() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB1_NC2.sql";
        executeSQLScript(scriptFilePath, conn);
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
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB1_NC3.sql";
        executeSQLScript(scriptFilePath, conn);
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
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB2_NC1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->eBookDAO.findByCoWriter(10));
        assertEquals("author is not valid", e.getMessage());
    }

    @Test
    void findByCoWriter_IA2_ADB2_NC1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByCowriter_IA1_ADB2_NC1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->eBookDAO.findByCoWriter(-1));
        assertEquals("author id is not valid", e.getMessage());
    }





    @Test
    void findByMainWriter_IA1_ADB1_NC1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        Set<EBook> ebooks = eBookDAO.findByMainWriter(1);

        assertEquals(0, ebooks.size());

    }

    @Test
    void findByMainWriter_IA1_ADB1_NC2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC2.sql";
        executeSQLScript(scriptFilePath, conn);
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
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC3.sql";
        executeSQLScript(scriptFilePath, conn);
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
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC3.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->eBookDAO.findByMainWriter(10));
        assertEquals( "mainAuthor is not on database",e.getMessage());

    }

    @Test
    void findByMainWriter_IA2_ADB2_NC1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/EbookDAOTest/findByMainwriter_IA1_ADB1_NC3.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->eBookDAO.findByMainWriter(-1));
        assertEquals( "mainAuthor id is not valid",e.getMessage());
    }

}
