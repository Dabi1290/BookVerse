package storageSubSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import proposalManager.Proposal;
import testing.ExtractStatementsFromScript;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;

public class ProposalDAOTest {

    private ProposalDAO proposalDao;
    private Connection conn;
    private DataSource ds;



    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
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

        catch(SQLException ex) {
            System.out.println(ex.getSQLState());
            //System.out.println(ex.getErrorCode());
        }
    }

    @Test
    public void newProposal_PV1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/newProposal_PV1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        //Create a proposal
        Proposal proposal = new Proposal();
        Author mainAuthor = new Author();
        mainAuthor.setId(1);
        Set<Author> coAuthors = new HashSet<>();
        Author author = new Author();
        author.setId(2);
        coAuthors.add(author);
        proposal.setProposedBy(mainAuthor);
        proposal.setCollaborators(coAuthors);
        //Create a proposal


        proposalDao = new ProposalDAO(ds);

        assertEquals(2, proposalDao.newProposal(proposal));
    }

    @Test
    public void newProposal_PV2() throws SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/newProposal_PV2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        proposalDao = new ProposalDAO(ds);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.newProposal(null));
        assertEquals(ex.getMessage(), "Can't create a null proposal in the database");
    }









    @Test
    public void findByCoAuthor_IP1_IC1_NP1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC1_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int coAuthorId = 4;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(coAuthorId)).thenReturn(new Author());
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        Set<Proposal> proposals = proposalDao.findByCoAuthor(coAuthorId);
        assertTrue(proposals.isEmpty());
    }

    @Test
    public void findByCoAuthor_IP1_IC1_NP2() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC1_NP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        //set the oracle
        Set<Proposal> expectedProposals = new HashSet<>();
        Proposal proposal = new Proposal();
        proposal.setId(1);
        expectedProposals.add(proposal);
        //set the oracle

        int coAuthorId = 3;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(coAuthorId)).thenReturn(new Author());
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        Set<Proposal> proposals = proposalDao.findByCoAuthor(coAuthorId);
        assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByCoAuthor_IP1_IC1_NP3() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC1_NP3.sql";
        executeSQLscript(scriptFilePath);
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

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(coAuthorId)).thenReturn(new Author());
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        Set<Proposal> proposals = proposalDao.findByCoAuthor(coAuthorId);
        assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByCoAuthor_IP1_IC2_NP1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP1_IC2_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        int coAuthorId = 6;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(coAuthorId)).thenReturn(null);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByCoAuthor(coAuthorId));
        assertEquals(ex.getMessage(), "Doesn't exist an author with this id");
    }

    @Test
    public void findByCoAuthor_IP2_IC2_NP1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByCoAuthor_IP2_IC2_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        int coAuthorId = 0;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(coAuthorId)).thenReturn(null);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByCoAuthor(coAuthorId));
        assertEquals(ex.getMessage(), "not a valid value for id");
    }











    @Test
    public void findByMainAuthor_IP1_IC1_NP1() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByMainAuthor_IP1_IC1_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int mainAuthorId = 2;

        Author mainAuthor = new Author();
        mainAuthor.setId(mainAuthorId);

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(mainAuthorId)).thenReturn(mainAuthor);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        assertTrue(proposalDao.findByMainAuthor(mainAuthorId).isEmpty());
    }

    @Test
    public void findByMainAuthor_IP1_IC1_NP2() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByMainAuthor_IP1_IC1_NP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int mainAuthorId = 1;

        //Create oracle
        Author mainAuthor = new Author();
        mainAuthor.setId(mainAuthorId);

        Set<Proposal> expectedProposals = new HashSet<>();
        Proposal proposal = new Proposal();
        proposal.setId(1);
        expectedProposals.add(proposal);
        //Create oracle

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(mainAuthorId)).thenReturn(mainAuthor);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        Set<Proposal> proposals = proposalDao.findByMainAuthor(mainAuthorId);
        assertTrue(expectedProposals.containsAll(proposals) && proposals.containsAll(expectedProposals));
    }

    @Test
    public void findByMainAuthor_IP1_IC1_NP3() throws SQLException, InvalidParameterException {
        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByMainAuthor_IP1_IC1_NP3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int mainAuthorId = 1;

        //Create oracle
        Author mainAuthor = new Author();
        mainAuthor.setId(mainAuthorId);

        Set<Proposal> expectedProposals = new HashSet<>();
        Proposal proposal = new Proposal();
        proposal.setId(1);
        expectedProposals.add(proposal);
        proposal = new Proposal();
        proposal.setId(2);
        expectedProposals.add(proposal);
        //Create oracle

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(mainAuthorId)).thenReturn(mainAuthor);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        Set<Proposal> proposals = proposalDao.findByMainAuthor(mainAuthorId);
        assertTrue(expectedProposals.containsAll(proposals) && proposals.containsAll(expectedProposals));
    }

    @Test
    public void findByMainAuthor_IP1_IC2_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByMainAuthor_IP1_IC2_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int mainAuthorId = 4;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(mainAuthorId)).thenReturn(null);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByMainAuthor(mainAuthorId));
        assertEquals(ex.getMessage(), "Doesn't exist an author with this id");
    }

    @Test
    public void findByMainAuthor_IP2_IC2_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByMainAuthor_IP2_IC2_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int mainAuthorId = -1;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.when(authorDAO.findByID(mainAuthorId)).thenReturn(null);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByMainAuthor(mainAuthorId));
        assertEquals(ex.getMessage(), "not a valid value for id");
    }

    @Test
    public void findByValidator_IP1_VDB1_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB1_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int validatorId = 2;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validatorId)).thenReturn(new Validator());

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        assertTrue(proposalDao.findByValidator(validatorId).isEmpty());
    }

    @Test
    public void findByValidator_IP1_VDB1_NP2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB1_NP2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int validatorId = 2;

        Validator validator = new Validator();
        validator.setId(validatorId);

        //Create oracle
        Set<Proposal> expectedProposals = new HashSet<>();
        Proposal proposal = new Proposal();
        proposal.setId(1);
        expectedProposals.add(proposal);
        //Create oracle

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validatorId)).thenReturn(new Validator());

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        Set<Proposal> proposals = proposalDao.findByValidator(validatorId);
        Assertions.assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByValidator_IP1_VDB1_NP3() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB1_NP3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int validatorId = 2;

        Validator validator = new Validator();
        validator.setId(2);

        //Create oracle
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
        //Create oracle

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validatorId)).thenReturn(new Validator());

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        Set<Proposal> proposals = proposalDao.findByValidator(validatorId);
        assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByValidator_IP1_VDB2_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB2_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int validatorId = 2;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validatorId)).thenReturn(null);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByValidator(validatorId));
        assertEquals(ex.getMessage(), "This validator doesn't exist on database");
    }

    @Test
    public void findByValidator_IP2_VDB2_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP2_VDB2_NP1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        int validatorId = 0;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validatorId)).thenReturn(null);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByValidator(validatorId));
        assertEquals(ex.getMessage(), "Value not valid for validator");
    }
}