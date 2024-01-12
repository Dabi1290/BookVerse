package storageSubSystem;

import proposalManager.Proposal;
import userManager.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
}
