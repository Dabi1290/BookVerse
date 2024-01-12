package storageSubSystem;

import proposalManager.Proposal;
import proposalManager.Version;
import userManager.Author;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProposalDAO {
    private DataSource ds=null;
    public ProposalDAO(DataSource ds) {
        this.ds=ds;
    }
    public Set<Proposal> findByValidator(int vid) throws SQLException {
        String query="SELECT * FROM Proposal JOIN ProposalValidator as p ON proposalId_fk=id WHERE p.validatorId_fk=?";

        Connection c=ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1,vid);

        ResultSet rs = ps.executeQuery();
        TreeSet<Proposal> s=new TreeSet<>();
        while(rs.next()){
            Proposal p = new Proposal();

            p.setId(rs.getInt("id"));
            p.setStatus(rs.getString("status"));

            //CHECK: probabilmente bisogna aggiungere il recupero delle versioni già qui

            s.add(p);
        }

        return s;
    }
    public Set<Proposal> findByAuthor(int authorId) throws SQLException {

        String query = "SELECT * FROM Proposal JOIN ProposalAuthor as p ON proposalId_fk=id WHERE p.authorId_fk=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, authorId);

        ResultSet rs = ps.executeQuery();
        TreeSet<Proposal> s = new TreeSet<>();
        while(rs.next()) {
            Proposal p = new Proposal();

            p.setId(rs.getInt("id"));
            p.setStatus(rs.getString("status"));

            //CHECK: probabilmente bisogna aggiungere il recupero delle versioni già qui

            s.add(p);
        }

        c.close();

        return s;
    }

    public List<Version> findProposalVersions(int proposalId) throws SQLException {
        List<Version> versions = new ArrayList<>();

        String query = "SELECT V.id, title, description, price, coverImage, report, ebookFile, data FROM Proposal as P, Version as V WHERE P.id=V.proposalId_fk AND P.id=? ORDER BY data ASC";

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
            //CHECK add data
            int versionId = rs.getInt("V.id");



            String queryForGenres = "SELECT name FROM Version as V, VersionGenre as VG, Genre as G WHERE VG.versionId_fk = V.id AND VG.genreId_fk = G.id AND V.id=?";
            PreparedStatement psForGenres = c.prepareStatement(queryForGenres);
            psForGenres.setInt(1, versionId);
            ResultSet resultSetGenres = psForGenres.executeQuery();
            Set<String> genres = new TreeSet<>();
            while(resultSetGenres.next()) {
                String genre = resultSetGenres.getString("name");
                genres.add(genre);
            }


            
            //CHECK substitute data
            Version version = Version.makeVersion(versionId, title, description, price, new File(reportPath), new File(ebookFilePath), new File(coverImagePath), new Date(), genres);
            versions.add(version);
        }

        c.close();

        return versions;
    }
}
