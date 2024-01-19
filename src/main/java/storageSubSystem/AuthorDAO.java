package storageSubSystem;

import proposalManager.Proposal;
import userManager.Author;
import userManager.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class AuthorDAO {
    private DataSource ds=null;
    public AuthorDAO(DataSource ds) {
        this.ds=ds;
    }

    public Author findByID(int id) throws InvalidParameterException, SQLException {

        if(id <= 0)
            throw new InvalidParameterException("The value of id is not valid");


        Author a=null;

        String query = "SELECT * FROM Author WHERE id=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1,id);

        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            a = new Author(id);
        }

        c.close();

        return a;
    }

    public Set<User> findAuthorsByEmail(String email) throws SQLException, InvalidParameterException {

        if(email == null || email.isEmpty())
            throw new InvalidParameterException("email is not valid");


        email=email+"%";
        String query = "SELECT * FROM Author, User WHERE Author.id=User.id AND User.email LIKE ?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        Set<User> authors = new HashSet<>();

        while(rs.next()) {
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String email_ = rs.getString("email");
            String password = rs.getString("password");
            int id = rs.getInt("id");
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setSurname(surname);
            user.setEmail(email_);
            user.setPassword(password);

            authors.add(user);
        }

        c.close();
        
        return authors;
    }

    public Set<Author> findCoAuthorsForProposal(Proposal proposal) throws SQLException, InvalidParameterException {

        //Check if parameters are valid
        if(proposal == null || proposal.getId() <= 0)
            throw new InvalidParameterException("proposal parameter is not valid");

        if ( new ProposalDAO(ds).findById(proposal.getId()) == null)
            throw new InvalidParameterException("proposal doesn't exist on database");
        //Check if parameters are valid



        String query = "SELECT * FROM Proposal as P, ProposalAuthor as PA, User as U WHERE P.id = PA.proposalId_fk AND U.id = PA.authorId_fk AND P.id = ?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, proposal.getId());
        ResultSet rs = ps.executeQuery();

        Set<Author> coAuthors = new HashSet<>();

        while(rs.next()) {
            User user = User.makeUser(rs.getInt("PA.authorId_fk"), rs.getString("name"), rs.getString("surname"), rs.getString("email"));

            Author author = new Author();
            author.setId(user.getId());
            author.setUser(user);

            author.setAlreadyLoadedUser(true);
            coAuthors.add(author);
        }

        c.close();

        return coAuthors;
    }

    public Author findMainAuthorForProposal(Proposal proposal) throws SQLException, InvalidParameterException {

        //Check if parameters are valid
        if(proposal == null || proposal.getId() <= 0)
            throw new InvalidParameterException("proposal parameter is not valid");

        if ( new ProposalDAO(ds).findById(proposal.getId()) == null)
            throw new InvalidParameterException("proposal doesn't exist on database");
        //Check if parameters are valid



        String query = "SELECT * FROM Proposal as P, User as U WHERE P.mainAuthorId_fk = U.id AND P.id = ?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1,proposal.getId());
        ResultSet rs = ps.executeQuery();

        Author author = null;
        if(rs.next()) {
            User user = User.makeUser(rs.getInt("U.id"), rs.getString("name"), rs.getString("surname"), rs.getString("email"));

            author = new Author();
            author.setId(user.getId());
            author.setUser(user);
        }

        c.close();

        return author;
    }
}
