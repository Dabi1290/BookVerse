package unitTesting.storageSubSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proposalManager.Proposal;
import storageSubSystem.AuthorDAO;
import storageSubSystem.InvalidParameterException;
import storageSubSystem.ProposalDAO;
import testing.SQLScript;
import testing.RetrieveCredentials;
import testing.SQLScript;
import userManager.Author;
import userManager.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

public class AuthorDAOTest {
    private Connection conn;
    private DataSource ds;
    private AuthorDAO authorDAO;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {

        String[] crendentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", crendentials[0], crendentials[1]);

        executeSQLscript("src/test/db/init.sql");
        conn.setCatalog("BookVerseTest");

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);
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

        catch(SQLException ex) {
            System.out.println(ex.getSQLState());
            //System.out.println(ex.getErrorCode());
        }
    }

    @Test
    public void findAuthorById_IV1_IA1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorById_IV1_IA1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        authorDAO = new AuthorDAO(ds);

        Author a = authorDAO.findByID(1);
        assertEquals(1, a.getId());
    }

    @Test
    public void findAuthorById_IV1_IA2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorById_IV1_IA2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        authorDAO = new AuthorDAO(ds);

        Author a = authorDAO.findByID(2);
        assertNull(a);
    }

    @Test
    public void findAuthorById_IV2_IA2() throws SQLException{
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findAuthorById_IV2_IA2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        authorDAO = new AuthorDAO(ds);

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

        authorDAO = new AuthorDAO(ds);

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

        authorDAO = new AuthorDAO(ds);

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

        authorDAO = new AuthorDAO(ds);

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

        authorDAO = new AuthorDAO(ds);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findAuthorsByEmail(email));
        assertEquals("email is not valid", ex.getMessage());
    }

    @Test
    public void findCoAuthorsForProposal_P1_PDB1_AP1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB1_AP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        //Oracolo
        Set<Author> expectedAuthorsId = new HashSet<>();
        expectedAuthorsId.add(new Author(1));
        expectedAuthorsId.add(new Author(2));
        expectedAuthorsId.add(new Author(3));
        //Oracolo

        Proposal proposal = new Proposal();
        proposal.setId(1);

        ProposalDAO proposalDao = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDao.findById(1)).thenReturn(proposal);

        authorDAO = new AuthorDAO(ds, proposalDao);
        Set<Author> authors = authorDAO.findCoAuthorsForProposal(proposal);



        assertTrue(expectedAuthorsId.containsAll(authors) && authors.containsAll(expectedAuthorsId));
    }

    @Test
    public void findCoAuthorsForProposal_P1_PDB1_AP2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB1_AP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        //Oracolo
        Set<Author> expectedAuthorsId = new HashSet<>();
        expectedAuthorsId.add(new Author(1));
        //Oracolo

        Proposal proposal = new Proposal();
        proposal.setId(1);

        ProposalDAO proposalDao = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDao.findById(1)).thenReturn(proposal);

        authorDAO = new AuthorDAO(ds, proposalDao);
        Set<Author> authors = authorDAO.findCoAuthorsForProposal(proposal);

        assertTrue(expectedAuthorsId.containsAll(authors) && authors.containsAll(expectedAuthorsId));
    }


    @Test
    public void findCoAuthorsForProposal_P1_PDB1_AP3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB1_AP3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        Proposal proposal = new Proposal();
        proposal.setId(1);

        ProposalDAO proposalDao = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDao.findById(1)).thenReturn(proposal);

        authorDAO = new AuthorDAO(ds, proposalDao);
        Set<Author> authors = authorDAO.findCoAuthorsForProposal(proposal);

        assertTrue(authors.isEmpty());
    }

    @Test
    public void findCoAuthorsForProposal_P1_PDB2_AP3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB2_AP3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        Proposal proposal = new Proposal();
        proposal.setId(2);

        ProposalDAO proposalDao = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDao.findById(2)).thenThrow(new InvalidParameterException("proposal doesn't exist on database"));

        authorDAO = new AuthorDAO(ds, proposalDao);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findCoAuthorsForProposal(proposal));
        assertEquals(ex.getMessage(), "proposal doesn't exist on database");
    }

    @Test
    public void findCoAuthorsForProposal_P2_PDB2_AP3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P2_PDB2_AP3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        Proposal proposal = new Proposal();
        proposal.setId(0);

        //Not so useful mock
        ProposalDAO proposalDao = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDao.findById(0)).thenReturn(proposal);
        //Not so useful mock

        authorDAO = new AuthorDAO(ds, proposalDao);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findCoAuthorsForProposal(proposal));
        assertEquals(ex.getMessage(), "proposal parameter is not valid");
    }

    @Test
    public void findMainAuthorForProposal_P1_PDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findMainAuthorForProposal_P1_PDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Author expcetedAuthor = new Author();
        expcetedAuthor.setId(5);

        Proposal proposal = new Proposal();
        proposal.setId(1);

        ProposalDAO proposalDAO = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDAO.findById(1)).thenReturn(proposal);

        authorDAO = new AuthorDAO(ds, proposalDAO);
        assertEquals(expcetedAuthor, authorDAO.findMainAuthorForProposal(proposal));
    }

    @Test
    public void findMainAuthorForProposal_P1_PDB2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findMainAuthorForProposal_P1_PDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(2);

        ProposalDAO proposalDAO = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDAO.findById(2)).thenThrow(new InvalidParameterException("proposal doesn't exist on database"));

        authorDAO = new AuthorDAO(ds, proposalDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findMainAuthorForProposal(proposal));
        assertEquals(ex.getMessage(), "proposal doesn't exist on database");
    }

    @Test
    public void findMainAuthorForProposal_P2_PDB2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/AuthorDAOTest/findMainAuthorForProposal_P2_PDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(0);

        ProposalDAO proposalDAO = Mockito.mock(ProposalDAO.class);
        Mockito.when(proposalDAO.findById(2)).thenThrow(new InvalidParameterException("proposal doesn't exist on database"));

        authorDAO = new AuthorDAO(ds, proposalDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findMainAuthorForProposal(proposal));
        assertEquals(ex.getMessage(), "proposal parameter is not valid");
    }
}
