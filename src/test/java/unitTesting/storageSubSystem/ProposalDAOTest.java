package unitTesting.storageSubSystem;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import proposalManager.Proposal;
import proposalManager.Version;
import storageSubSystem.AuthorDAO;
import storageSubSystem.InvalidParameterException;
import storageSubSystem.ProposalDAO;
import storageSubSystem.ValidatorDAO;
import testing.SQLScript;
import testing.RetrieveCredentials;
import testing.SQLScript;
import userManager.Author;
import userManager.Validator;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    public Connection newConnection() throws SQLException {
        String[] credentials = RetrieveCredentials.retrieveCredentials("src/test/credentials.xml");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookVerseTest", credentials[0], credentials[1]);
        connection.setCatalog("BookVerseTest");

        return connection;
    }

    @Test
    public void newProposal_PV1() throws InvalidParameterException, SQLException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/newProposal_PV1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        //Create a proposal
        int expectedProposalId = 2;

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

        assertEquals(expectedProposalId, proposalDao.newProposal(proposal));



        //Controll if query modifed the database correctly
        String query = "SELECT * FROM Proposal as P WHERE P.id = ?";

        Connection c = newConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, expectedProposalId);
        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals("Pending", rs.getString("status"));

        assertEquals(proposal.getProposedBy().getId(), rs.getInt("mainAuthorId_fk"));




        query = "SELECT * FROM ProposalAuthor as PA WHERE PA.proposalId_fk = ?";

        ps = c.prepareStatement(query);
        ps.setInt(1, expectedProposalId);
        rs = ps.executeQuery();

        Set<Integer> idCoAuthors = new TreeSet<>();

        while(rs.next()) {
            idCoAuthors.add(rs.getInt("authorId_fk"));
        }

        assertEquals(proposal.getCollaborators().size(), idCoAuthors.size());

        for(Author coAuthor : proposal.getCollaborators()) {
            assertTrue(idCoAuthors.contains(coAuthor.getId()));
        }

        c.close();
        //Controll if query modifed the database correctly
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



    @Test
    public void newVersion_PV1_VV1_PDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/newVersion_PV1_VV1_PDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        //Prepare oracle and input
        int expectedVersion = 2;


        int proposalId = 1;

        Proposal proposal = new Proposal();
        proposal.setId(proposalId);

        Version version = new Version();
        version.setTitle("Titolo1");
        version.setDescription("Descrizione1");
        version.setPrice(0);
        version.setDate(LocalDate.now());
        version.setReport(null);
        version.setEbookFile(null);
        version.setCoverImage(null);

        Set<String> genres = new HashSet<>();
        genres.add("genere1");
        genres.add("genere2");
        version.setGenres(genres);
        //Prepare oracle and input


        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        assertEquals(expectedVersion, proposalDao.newVersion(proposal, version));


        //Control if the method created the version correctly
        String query = "SELECT * FROM Version as V WHERE V.id = ?";


        Connection c = newConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, expectedVersion);
        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(version.getTitle(), rs.getString("title"));
        assertEquals(version.getDescription(), rs.getString("description"));
        assertEquals(version.getPrice(), rs.getInt("price"));
        assertNull(rs.getString("ebookFile"));
        assertNull(rs.getString("report"));
        assertNull(rs.getString("coverImage"));
        assertEquals(proposalId, rs.getInt("proposalId_fk"));
        assertEquals(version.getDate(), LocalDate.parse(rs.getString("data")));
        //Control if the method created the version correctly
    }

    @Test
    public void newVersion_PV1_VV1_PDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/newVersion_PV1_VV1_PDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        int proposalId = 10;

        Proposal proposal = new Proposal();
        proposal.setId(proposalId);

        Version version = new Version();
        version.setTitle("Titolo1");
        version.setDescription("Descrizione1");
        version.setPrice(0);
        version.setDate(LocalDate.now());
        version.setReport(null);
        version.setEbookFile(null);
        version.setCoverImage(null);

        Set<String> genres = new HashSet<>();
        genres.add("genere1");
        genres.add("genere2");
        version.setGenres(genres);



        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.newVersion(proposal, version));
        assertEquals(ex.getMessage(), "This proposal doesn't exist on database");
    }

    @Test
    public void newVersion_PV1_VV2_PDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/newVersion_PV1_VV2_PDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        int proposalId = 10;

        Proposal proposal = new Proposal();
        proposal.setId(proposalId);

        Version version = null;



        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.newVersion(proposal, version));
        assertEquals(ex.getMessage(), "null is not a valid value for version");
    }

    @Test
    public void newVersion_PV2_VV1_PDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/newVersion_PV2_VV1_PDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database




        Proposal proposal = null;

        Version version = null;

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.newVersion(proposal, version));
        assertEquals(ex.getMessage(), "null is not a valid value for proposal");
    }





    @Test
    public void updateVersion_PV1_VBD2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/updateVersion_PV1_VDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        int versionId = 15;
        Version version = new Version();
        version.setId(versionId);

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.updateVersion(version));
        assertEquals(ex.getMessage(), "this version isn't on database");
    }

    @Test
    public void updateVersion_PV2_VBD2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/updateVersion_PV2_VDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        Version version = null;
        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.updateVersion(version));
        assertEquals(ex.getMessage(), "version can't be null");
    }

    @Test
    public void updateVersion_PV1_VBD1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/updateVersion_PV1_VDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        int versionId = 1;
        Version version = new Version();
        version.setId(versionId);
        version.setTitle("Titolo1");
        version.setDescription("Descrizione1");
        version.setDate(LocalDate.now());


        Set<String> genres = new TreeSet<>();
        genres.add("genere1");
        version.setGenres(genres);

        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        proposalDao.updateVersion(version);


        //Retrieve from database for test
    }

    @Test
    public void assignValidator_PV1_VV1_PDB1_VDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV1_PDB1_VDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        int proposalId = 1;
        int validatorId = 1;
        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        Validator validator = new Validator();
        validator.setId(validatorId);



        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validatorId)).thenReturn(validator);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        proposalDao.assignValidator(proposal, validator);


        //Control if the method modified correctly the database
        String query = "SELECT * FROM ProposalValidator as PV WHERE PV.validatorId_fk = ? AND PV.proposalId_fk = ?";

        Connection c = newConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, validatorId);
        ps.setInt(2, proposalId);
        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());

        c.close();
        //Control if the method modified correctly the database
    }

    @Test
    public void assignValidator_PV1_VV1_PDB1_VDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV1_PDB1_VDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(1);
        Validator validator = new Validator();
        validator.setId(2);



        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validator.getId())).thenReturn(null);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "This validator doesn't exist on database");
    }

    @Test
    public void assignValidator_PV1_VV1_PDB2_VDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV1_PDB2_VDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(2);
        Validator validator = new Validator();
        validator.setId(1);



        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.when(validatorDAO.findValidatorById(validator.getId())).thenReturn(validator);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "This proposal doesn't exist on database");
    }

    @Test
    public void assignValidator_PV1_VV2_PDB1_VDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV2_PDB1_VDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(1);
        Validator validator = new Validator();
        validator.setId(-1);



        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "value not valid for validator");
    }

    @Test
    public void assignValidator_PV2_VV1_PDB2_VDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV2_VV1_PDB2_VDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(0);
        Validator validator = new Validator();
        validator.setId(1);



        AuthorDAO authorDAO = Mockito.mock(AuthorDAO.class);
        ValidatorDAO validatorDAO = Mockito.mock(ValidatorDAO.class);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "value not valid for proposal");
    }



    @Test
    public void findById_IP1_PDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findById_IP1_PDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        //Expected proposal
        int proposalId = 1;

        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        //Expected proposal

        proposalDao = new ProposalDAO(ds);

        assertEquals(proposal.getId(), proposalDao.findById(proposalId).getId());
    }

    @Test
    public void findById_IP1_PDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findById_IP1_PDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        //Expected proposal
        int proposalId = 3;

        Proposal proposal = new Proposal();
        proposal.setId(proposalId);

        proposalDao = new ProposalDAO(ds);

        assertNull(proposalDao.findById(proposalId));
        //Expected proposal
    }

    @Test
    public void findById_IP2_PDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/findById_IP2_PDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        //Expected proposal
        int proposalId = 0;

        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        //Expected proposal

        proposalDao = new ProposalDAO(ds);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findById(proposalId));
        assertEquals(ex.getMessage(), "id value is not valid");
    }



    @Test
    public void updateProposalState_I1_PDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/updateProposalState_I1_PDB1.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database


        //Create oracle
        int proposalId = 1;
        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        proposal.setStatus("Refused");
        //Create oracle

        proposalDao = new ProposalDAO(ds);

        proposalDao.updateProposalState(proposal);


        //Controll if effectively updated the state of proposal
        String query = "SELECT * FROM Proposal as P WHERE P.id = ?";
        Connection c = newConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, proposalId);

        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(proposal.getStatus(), rs.getString("status"));

        c.close();
        //Controll if effectively updated the state of proposal
    }


    @Test
    public void updateProposalState_I1_PDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/updateProposalState_I1_PDB2.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        int proposalId = 1;
        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        proposal.setStatus("Approved");

        proposalDao = new ProposalDAO(ds);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.updateProposalState(proposal));
        assertEquals(ex.getMessage(), "Can't pass from status of proposal on database to state the of proposal");
    }

    @Test
    public void updateProposalState_I1_PDB3() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/updateProposalState_I1_PDB3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        int proposalId = 3;
        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        proposal.setStatus("Approved");

        proposalDao = new ProposalDAO(ds);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.updateProposalState(proposal));
        assertEquals(ex.getMessage(), "This proposal doesn't exist on database");
    }

    @Test
    public void updateProposalState_I2_PDB3() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLscript("src/test/db/createDbForTest.sql");
        String scriptFilePath = "src/test/db/ProposalDAOTest/updateProposalState_I2_PDB3.sql";
        executeSQLscript(scriptFilePath);
        //Prepare database



        Proposal proposal = null;

        proposalDao = new ProposalDAO(ds);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.updateProposalState(proposal));
        assertEquals(ex.getMessage(), "proposal value is not valid");
    }
}