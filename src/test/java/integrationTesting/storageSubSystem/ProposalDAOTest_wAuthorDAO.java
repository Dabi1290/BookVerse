package integrationTesting.storageSubSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import proposalManager.Proposal;
import storageSubSystem.AuthorDAO;
import storageSubSystem.InvalidParameterException;
import storageSubSystem.ProposalDAO;
import storageSubSystem.ValidatorDAO;
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

public class ProposalDAOTest_wAuthorDAO {

    private Connection conn;
    private DataSource ds;
    private ProposalDAO proposalDao;



    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
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


    private Connection newConnection() throws SQLException {
        String[] credentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest", credentials[0], credentials[1]);
        connection.setCatalog("BookVerseTest");

        return connection;
    }

    public void tearDown() throws SQLException {
        conn.close();
    }



    
    
    @Test
    public void findByCoAuthor_IP1_IC1_NP1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC1_NP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        int coAuthorId = 4;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        Set<Proposal> proposals = proposalDao.findByCoAuthor(coAuthorId);
        assertTrue(proposals.isEmpty());
    }

    @Test
    public void findByCoAuthor_IP1_IC1_NP2() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC1_NP2.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        //set the oracle
        Set<Proposal> expectedProposals = new HashSet<>();
        Proposal proposal = new Proposal();
        proposal.setId(1);
        expectedProposals.add(proposal);
        //set the oracle

        int coAuthorId = 3;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        Set<Proposal> proposals = proposalDao.findByCoAuthor(coAuthorId);
        assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByCoAuthor_IP1_IC1_NP3() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC1_NP3.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        //set the oracle
        Set<Proposal> expectedProposals = new HashSet<>();
        Proposal proposal = new Proposal();
        proposal.setId(1);
        expectedProposals.add(proposal);
        proposal = new Proposal();
        proposal.setId(2);
        expectedProposals.add(proposal);
        proposal = new Proposal();
        proposal.setId(3);
        expectedProposals.add(proposal);
        //set the oracle

        int coAuthorId = 3;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        Set<Proposal> proposals = proposalDao.findByCoAuthor(coAuthorId);
        assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByCoAuthor_IP1_IC2_NP1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC2_NP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        int coAuthorId = 6;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByCoAuthor(coAuthorId));
        assertEquals(ex.getMessage(), "Doesn't exist an author with this id");
    }

    @Test
    public void findByCoAuthor_IP2_IC2_NP1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP2_IC2_NP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        int coAuthorId = 0;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByCoAuthor(coAuthorId));
        assertEquals(ex.getMessage(), "not a valid value for id");
    }




    
}
