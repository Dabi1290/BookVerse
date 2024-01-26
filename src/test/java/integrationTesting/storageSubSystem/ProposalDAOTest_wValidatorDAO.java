package integrationTesting.storageSubSystem;

import org.junit.jupiter.api.Assertions;
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
import static testing.SQLScript.*;
import userManager.Validator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProposalDAOTest_wValidatorDAO {

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





    @Test
    public void findByValidator_IP1_VDB1_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB1_NP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        int validatorId = 2;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        assertTrue(proposalDao.findByValidator(validatorId).isEmpty());
    }

    @Test
    public void findByValidator_IP1_VDB1_NP2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB1_NP2.sql";
        executeSQLScript(scriptFilePath, conn);
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

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        Set<Proposal> proposals = proposalDao.findByValidator(validatorId);
        Assertions.assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByValidator_IP1_VDB1_NP3() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB1_NP3.sql";
        executeSQLScript(scriptFilePath, conn);
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

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        Set<Proposal> proposals = proposalDao.findByValidator(validatorId);
        assertTrue(proposals.containsAll(expectedProposals) && expectedProposals.containsAll(proposals));
    }

    @Test
    public void findByValidator_IP1_VDB2_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP1_VDB2_NP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        int validatorId = 2;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByValidator(validatorId));
        assertEquals(ex.getMessage(), "This validator doesn't exist on database");
    }

    @Test
    public void findByValidator_IP2_VDB2_NP1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/findByValidator_IP2_VDB2_NP1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        int validatorId = 0;

        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);
        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.findByValidator(validatorId));
        assertEquals(ex.getMessage(), "Value not valid for validator");
    }





    @Test
    public void assignValidator_PV1_VV1_PDB1_VDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV1_PDB1_VDB1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database


        int proposalId = 1;
        int validatorId = 1;
        Proposal proposal = new Proposal();
        proposal.setId(proposalId);
        Validator validator = new Validator();
        validator.setId(validatorId);



        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);
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
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV1_PDB1_VDB2.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(1);
        Validator validator = new Validator();
        validator.setId(2);



        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "This validator doesn't exist on database");
    }

    @Test
    public void assignValidator_PV1_VV1_PDB2_VDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV1_PDB2_VDB1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(2);
        Validator validator = new Validator();
        validator.setId(1);



        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "This proposal doesn't exist on database");
    }

    @Test
    public void assignValidator_PV1_VV2_PDB1_VDB2() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV1_VV2_PDB1_VDB2.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(1);
        Validator validator = new Validator();
        validator.setId(-1);



        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "value not valid for validator");
    }

    @Test
    public void assignValidator_PV2_VV1_PDB2_VDB1() throws SQLException, InvalidParameterException {

        //Prepare database
        executeSQLScript("src/test/db/createDbForTest.sql", conn);
        String scriptFilePath = "src/test/db/ProposalDAOTest/assignValidator_PV2_VV1_PDB2_VDB1.sql";
        executeSQLScript(scriptFilePath, conn);
        //Prepare database

        Proposal proposal = new Proposal();
        proposal.setId(0);
        Validator validator = new Validator();
        validator.setId(1);



        AuthorDAO authorDAO = new AuthorDAO(ds);
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);
        proposalDao = new ProposalDAO(ds, validatorDAO, authorDAO);

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () -> proposalDao.assignValidator(proposal, validator));
        assertEquals(ex.getMessage(), "value not valid for proposal");
    }

}
