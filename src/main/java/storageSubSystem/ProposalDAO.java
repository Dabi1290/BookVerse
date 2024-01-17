package storageSubSystem;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
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
import java.util.Date;

public class ProposalDAO {
    private DataSource ds=null;
    public ProposalDAO(DataSource ds) {
        this.ds=ds;
    }
    public Set<Proposal> findByValidator(int vid) throws SQLException {

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

            p.setVersions(this.findProposalVersions(p.getId()));

            s.add(p);
        }

        c.close();

        return s;
    }
    public Set<Proposal> findByCoAuthor(int authorId) throws SQLException {

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

            p.setVersions(this.findProposalVersions(p.getId()));

            s.add(p);
        }

        c.close();

        return s;
    }

    public Set<Proposal> findByMainAuthor(int mainAuthorId) throws SQLException {

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

            p.setVersions(this.findProposalVersions(p.getId()));

            proposals.add(p);
        }

        c.close();

        return proposals;
    }

    private List<Version> findProposalVersions(int proposalId) throws SQLException {
        List<Version> versions = new ArrayList<>();

        String query = "SELECT V.id, title, description, price, coverImage, report, ebookFile, data FROM Proposal as P, Version as V WHERE P.id=V.proposalId_fk AND P.id=? ORDER BY data DESC";

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
                ebookFile = new File(BaseFileDAO.getFilesDirectory() + "/" + Integer.toString(proposalId) + "/" + ebookFilePath);
            File coverImage = null;
            if(coverImagePath != null)
                coverImage = new File(BaseFileDAO.getFilesDirectory() + "/" + Integer.toString(proposalId) + "/" + coverImagePath);
            File reportFile = null;
            if(reportPath != null)
                reportFile = new File(BaseFileDAO.getFilesDirectory() + "/" + Integer.toString(proposalId) + "/" +reportPath);



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
            Version version = Version.makeVersion(versionId, title, description, price, reportFile, ebookFile, coverImage, d, genres);
            versions.add(version);
        }

        c.close();

        return versions;
    }

    public int newProposal(Proposal proposal) throws SQLException {

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
        else
            throw new SQLException("Failed to retrieve generated key");



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

    public int persistVersion(Proposal proposal, Version version) throws SQLException {

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

    public void updateVersion(Version version) throws SQLException {

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

    public Proposal findById(int id) throws SQLException {

        String query = "SELECT * FROM Proposal as P WHERE P.id=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        Proposal proposal = new Proposal();
        if(rs.next()) {
            proposal.setId(rs.getInt("id"));
            proposal.setStatus(rs.getString("status"));

            List<Version> versions = this.findProposalVersions(id);
            proposal.setVersions(versions);
        }
        else
            return null;

        c.close();

        return proposal;
    }

    public void updateProposalState(Proposal proposal) throws SQLException {

        String query = "UPDATE Proposal SET status = ? WHERE id = ?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, proposal.getStatus());
        ps.setInt(2, proposal.getId());
        ps.execute();

        c.close();
    }

    public void assignValidator(Proposal proposal, Validator validator) throws  SQLException {

        String query = "INSERT INTO ProposalValidator(validatorId_fk, proposalId_fk) values(?, ?)";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, validator.getId());
        ps.setInt(2, proposal.getId());
        ps.execute();

        c.close();
    }
}