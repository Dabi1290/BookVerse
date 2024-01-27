package integrationTesting.storageSubSystem;

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
import storageSubSystem.ProposalDAO;
import testing.RetrieveCredentials;
import userManager.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static testing.SQLScript.executeSQLScript;

public class AuthorDAOTest_wProposalDAO {

    private Connection conn;
    private DataSource ds;
    
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



    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.close();
    }



    @Test
    public void findCoAuthorsForProposal_P1_PDB1_AP1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB1_AP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        //Oracolo
        Set<Author> expectedAuthorsId = new HashSet<>();
        expectedAuthorsId.add(new Author(1));
        expectedAuthorsId.add(new Author(2));
        expectedAuthorsId.add(new Author(3));
        //Oracolo

        Proposal proposal = new Proposal();
        proposal.setId(1);

        ProposalDAO proposalDao = new ProposalDAO(ds);

        authorDAO = new AuthorDAO(ds, proposalDao);
        Set<Author> authors = authorDAO.findCoAuthorsForProposal(proposal);



        assertTrue(expectedAuthorsId.containsAll(authors) && authors.containsAll(expectedAuthorsId));
    }

    @Test
    public void findCoAuthorsForProposal_P1_PDB1_AP2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB1_AP2.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        //Oracolo
        Set<Author> expectedAuthorsId = new HashSet<>();
        expectedAuthorsId.add(new Author(1));
        //Oracolo

        Proposal proposal = new Proposal();
        proposal.setId(1);

        ProposalDAO proposalDao = new ProposalDAO(ds);

        authorDAO = new AuthorDAO(ds, proposalDao);
        Set<Author> authors = authorDAO.findCoAuthorsForProposal(proposal);

        assertTrue(expectedAuthorsId.containsAll(authors) && authors.containsAll(expectedAuthorsId));
    }


    @Test
    public void findCoAuthorsForProposal_P1_PDB1_AP3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB1_AP3.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database



        Proposal proposal = new Proposal();
        proposal.setId(1);

        ProposalDAO proposalDao = new ProposalDAO(ds);

        authorDAO = new AuthorDAO(ds, proposalDao);
        Set<Author> authors = authorDAO.findCoAuthorsForProposal(proposal);

        assertTrue(authors.isEmpty());
    }

    @Test
    public void findCoAuthorsForProposal_P1_PDB2_AP3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P1_PDB2_AP3.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database



        Proposal proposal = new Proposal();
        proposal.setId(2);

        ProposalDAO proposalDao = new ProposalDAO(ds);

        authorDAO = new AuthorDAO(ds, proposalDao);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findCoAuthorsForProposal(proposal));
        assertEquals(ex.getMessage(), "proposal doesn't exist on database");
    }

    @Test
    public void findCoAuthorsForProposal_P2_PDB2_AP3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/AuthorDAOTest/findCoAuthorsForProposal_P2_PDB2_AP3.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database



        Proposal proposal = new Proposal();
        proposal.setId(0);

        ProposalDAO proposalDao = new ProposalDAO(ds);

        authorDAO = new AuthorDAO(ds, proposalDao);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> authorDAO.findCoAuthorsForProposal(proposal));
        assertEquals(ex.getMessage(), "proposal parameter is not valid");
    }
}
