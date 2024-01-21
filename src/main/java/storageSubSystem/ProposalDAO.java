package storageSubSystem;

import jakarta.servlet.ServletException;
import proposalManager.Proposal;
import proposalManager.Version;
import userManager.Author;
import userManager.Validator;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProposalDAO {
    private ValidatorDAO validatorDAO;
    private AuthorDAO authorDAO;
    private DataSource ds=null;

    public ProposalDAO() {

    }

    public ProposalDAO(DataSource ds) {
        this.ds = ds;
    }
    public ProposalDAO(DataSource ds, ValidatorDAO validatorDAO, AuthorDAO authorDAO) {
        this.ds=ds;
        this.validatorDAO = validatorDAO;
        this.authorDAO = authorDAO;
    }

    public Set<Proposal> findByValidator(int vid) throws SQLException, InvalidParameterException {

        //Check if parameters are valid
        if(vid <= 0)
            throw new InvalidParameterException("Value not valid for validator");

        if(validatorDAO.findValidatorById(vid) == null)
            throw new InvalidParameterException("This validator doesn't exist on database");
        //Check if parameters are valid



        String query="SELECT * FROM Proposal JOIN ProposalValidator as p ON proposalId_fk=id WHERE p.validatorId_fk=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1,vid);

        ResultSet rs = ps.executeQuery();
        Set<Proposal> s=new HashSet<>();
        while(rs.next()){
            Proposal p = new Proposal();

            p.setId(rs.getInt("id"));
            p.setStatus(rs.getString("status"));
            s.add(p);

        }
        c.close();
        for(Proposal p : s) {
            p.setVersions(this.findProposalVersions(p.getId()));
        }


        return s;
    }
    public Set<Proposal> findByCoAuthor(int authorId) throws SQLException, InvalidParameterException {

        //Check parameters
        if(authorId <= 0)
            throw new InvalidParameterException("not a valid value for id");

        if(authorDAO.findByID(authorId) == null)
            throw new InvalidParameterException("Doesn't exist an author with this id");
        //Check parameters



        String query = "SELECT * FROM Proposal JOIN ProposalAuthor as p ON proposalId_fk=id WHERE p.authorId_fk=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, authorId);

        ResultSet rs = ps.executeQuery();
        Set<Proposal> s = new HashSet<>();
        while(rs.next()) {
            Proposal p = new Proposal();

            p.setId(rs.getInt("id"));
            p.setStatus(rs.getString("status"));



            s.add(p);
        }
        c.close();
        for(Proposal p:s){
            p.setVersions(this.findProposalVersions(p.getId()));
        }



        return s;
    }

    public Set<Proposal> findByMainAuthor(int mainAuthorId) throws SQLException, InvalidParameterException {

        //Check parameters
        if(mainAuthorId <= 0)
            throw new InvalidParameterException("not a valid value for id");

        if(authorDAO.findByID(mainAuthorId) == null)
            throw new InvalidParameterException("Doesn't exist an author with this id");
        //Check parameters



        String query = "SELECT * FROM Proposal WHERE mainAuthorId_fk=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, mainAuthorId);
        ResultSet rs = ps.executeQuery();

        Set<Proposal> proposals = new HashSet<>();

        while(rs.next()) {
            Proposal p = new Proposal();

            p.setId(rs.getInt("id"));
            p.setStatus(rs.getString("status"));



            proposals.add(p);
        }

        c.close();
        for(Proposal p: proposals){
            p.setVersions(this.findProposalVersions(p.getId()));
        }

        return proposals;
    }

    public int newProposal(Proposal proposal) throws SQLException, InvalidParameterException {

        //Check parameters
        if(proposal == null)
            throw new InvalidParameterException("Can't create a null proposal in the database");
        //Check parameters



        Author mainAuthor = proposal.getProposedBy();
        int mainAuthorId = mainAuthor.getId();

        String query = "INSERT INTO Proposal(status, mainAuthorId_fk) VALUES (?, ?)";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, "Pending");
        ps.setInt(2, mainAuthorId);
        ps.execute();



        int generatedId = 0;
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()) {
            generatedId = rs.getInt(1);
        }
        else {
            throw new SQLException("Failed to retrieve generated key");
        }


        Set<Author> coAuthors = proposal.getCollaborators();
        for(Author author : coAuthors) {
            int authorId = author.getId();

            String insertCoAuthors = "INSERT INTO ProposalAuthor(authorId_fk, proposalId_fk) values (?, ?)";

            PreparedStatement psForCoAuthors = c.prepareStatement(insertCoAuthors);
            psForCoAuthors.setInt(1, authorId);
            psForCoAuthors.setInt(2, generatedId);
            psForCoAuthors.execute();
        }

        c.close();

        return generatedId;
    }

    public int newVersion(Proposal proposal, Version version) throws SQLException, InvalidParameterException {

        //Check parameters
        if(proposal == null)
            throw new InvalidParameterException("null is not a valid value for proposal");

        if(version == null)
            throw new InvalidParameterException("null is not a valid value for version");

        if(proposal.getId() <= 0)
            throw new InvalidParameterException("Value of id is not valid");

        Proposal testP = findById(proposal.getId());
        if(testP == null)
            throw new InvalidParameterException("This proposal doesn't exist on database");
        //Check parameters



        String insertVersion = "INSERT INTO Version(title, description, price, data, proposalId_fk) values (?, ?, ?, ?, ?)";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(insertVersion, Statement.RETURN_GENERATED_KEYS);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = version.getDate().format(formatter);

        ps.setString(1, version.getTitle());
        ps.setString(2, version.getDescription());
        ps.setInt(3, version.getPrice());
        ps.setString(4, date);
        ps.setInt(5, proposal.getId());

        ps.execute();

        ResultSet rs = ps.getGeneratedKeys();
        int generatedId = 0;
        if(rs.next()) {
            generatedId = rs.getInt(1);
        }
        else{
            throw new SQLException("Tisca Tusca");
        }



        Set<String> genres = version.getGenres();
        for(String genre : genres) {
            String insertVersionGenres = "INSERT INTO VersionGenre(versionId_fk, genreId_fk) values (?, ?)";

            PreparedStatement genrePs = c.prepareStatement(insertVersionGenres);
            genrePs.setInt(1, generatedId);
            genrePs.setString(2, genre);
            genrePs.execute();
        }

        c.close();

        return generatedId;
    }

    public void updateVersion(Version version) throws SQLException, InvalidParameterException {

        //Check parameters
        if(version == null)
            throw new InvalidParameterException("version can't be null");

        if(version.getId() <= 0)
            throw new InvalidParameterException("not a valid value for id");

        if(findVersionById(version.getId()) == null)
            throw new InvalidParameterException("this version isn't on database");
        //Check parameters



        String query = "UPDATE Version SET title = ?, description = ?, price = ?, coverImage = ?, report = ?, ebookFile = ?, data = ? WHERE id = ?";

        Connection c = ds.getConnection();

        String report = null;
        if(version.getReport() != null)
            report = version.getReport().getName();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = version.getDate().format(formatter);

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, version.getTitle());
        ps.setString(2, version.getDescription());
        ps.setInt(3, version.getPrice());
        ps.setString(4, version.getCoverImage().getName());
        ps.setString(5, report);
        ps.setString(6, version.getEbookFile().getName());
        ps.setString(7, date);
        ps.setInt(8, version.getId());

        ps.execute();

        c.close();
    }

    public Proposal findById(int id) throws SQLException, InvalidParameterException {

        //Check parameters
        if(id <= 0)
            throw new InvalidParameterException("id value is not valid");
        //Check parameters



        String query = "SELECT * FROM Proposal as P WHERE P.id=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        Proposal proposal = new Proposal();
        if(rs.next()) {
            proposal.setId(rs.getInt("id"));
            proposal.setStatus(rs.getString("status"));
            c.close();
            List<Version> versions = this.findProposalVersions(id);
            proposal.setVersions(versions);
        }
        else {
            c.close();
            return null;
        }


        return proposal;
    }

    public void updateProposalState(Proposal proposal) throws SQLException, InvalidParameterException {

        //Check parameters
        if(proposal == null || proposal.getId() <= 0)
            throw new InvalidParameterException("proposal value is not valid");

        if(findById(proposal.getId()) == null)
            throw new InvalidParameterException("This proposal doesn't exist on database");
        //Check parameters



        String query = "UPDATE Proposal SET status = ? WHERE id = ?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, proposal.getStatus());
        ps.setInt(2, proposal.getId());
        ps.execute();

        c.close();
    }

    public void assignValidator(Proposal proposal, Validator validator) throws SQLException, InvalidParameterException {

        //Check parameters
        if(proposal == null || proposal.getId() <= 0)
            throw new InvalidParameterException("value not valid for proposal");

        if(validator == null || validator.getId() <= 0)
            throw new InvalidParameterException("value not valid for validator");

        if(findById(proposal.getId()) == null)
            throw new InvalidParameterException("This proposal doesn't exist on database");

        ValidatorDAO validatorDAO = new ValidatorDAO(ds);
        if(validatorDAO.findValidatorById(validator.getId()) == null)
            throw new InvalidParameterException("This validator doesn't exist on database");
        //Check parameters



        String query = "INSERT INTO ProposalValidator(validatorId_fk, proposalId_fk) values(?, ?)";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, validator.getId());
        ps.setInt(2, proposal.getId());
        ps.executeUpdate();

        c.close();
    }

    //ATTENTION: method not complete
    private Version findVersionById(int versionId) throws SQLException, InvalidParameterException {

        //Check parameters
        if(versionId <= 0)
            throw new InvalidParameterException("versionId is not valid");
        //Check parameters

        String query = "SELECT * FROM Version as V WHERE V.id = ?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, versionId);
        ResultSet rs = ps.executeQuery();

        c.close();

        Version version = new Version();
        if(rs.next()) {
            String title = rs.getString("title");
            String description = rs.getString("description");
            String coverImagePath = rs.getString("coverImage");
            String reportPath = rs.getString("report");
            String ebookFilePath = rs.getString("ebookFile");
            int price = rs.getInt("price");
            String date = rs.getString("data");

            version.setTitle(title);
            version.setDescription(description);
//            version.setCoverImage(new File(FileDAO.getFilesDirectory() + coverImagePath));
//            version.setEbookFile(new File(FileDAO.getFilesDirectory() + ebookFilePath));
//            version.setReport(new File(FileDAO.getFilesDirectory() + reportPath));
            version.setPrice(price);
        }

        return version;
    }

    private List<Version> findProposalVersions(int proposalId) throws SQLException, InvalidParameterException {
        List<Version> versions = new ArrayList<>();

        String query = "SELECT V.id, title, description, price, coverImage, report, ebookFile, data FROM Proposal as P, Version as V WHERE P.id=V.proposalId_fk AND P.id=? ORDER BY V.id DESC";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, proposalId);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            String title = rs.getString("title");
            String description = rs.getString("description");
            String coverImagePath = rs.getString("coverImage");
            String reportPath = rs.getString("report");
            String ebookFilePath = rs.getString("ebookFile");
            int price = rs.getInt("price");
            String date = rs.getString("data");
            int versionId = rs.getInt("V.id");



            File ebookFile = null;
            if(ebookFilePath != null)
                ebookFile = new File(FileDAO.getFilesDirectory() + "/" + Integer.toString(proposalId) + "/" + ebookFilePath);
            File coverImage = null;
            if(coverImagePath != null)
                coverImage = new File(FileDAO.getFilesDirectory() + "/" + Integer.toString(proposalId) + "/" + coverImagePath);
            File reportFile = null;
            if(reportPath != null)
                reportFile = new File(FileDAO.getFilesDirectory() + "/" + Integer.toString(proposalId) + "/" +reportPath);



            String queryForGenres = "SELECT genreId_fk FROM Version as V, VersionGenre as VG WHERE V.id=? AND V.id = VG.versionId_fk";
            PreparedStatement psForGenres = c.prepareStatement(queryForGenres);
            psForGenres.setInt(1, versionId);
            ResultSet resultSetGenres = psForGenres.executeQuery();
            Set<String> genres = new TreeSet<>();
            while(resultSetGenres.next()) {
                String genre = resultSetGenres.getString("genreId_fk");
                genres.add(genre);
            }



            LocalDate d = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            Version version = null;
            try {
                version = Version.makeVersion(versionId, title, description, price, reportFile, ebookFile, coverImage, d, genres);
            } catch (Exception e) {
                throw new InvalidParameterException("Parameters of version is not valid");
            }
            versions.add(version);
        }

        c.close();

        return versions;
    }
}