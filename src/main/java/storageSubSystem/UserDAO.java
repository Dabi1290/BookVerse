package storageSubSystem;

import proposalManager.Proposal;
import userManager.Author;
import userManager.User;
import userManager.Validator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class UserDAO {
    private DataSource ds=null;
    public UserDAO(DataSource ds) {
        this.ds=ds;
    }

    public User login(String email, String password, String role) throws SQLException {
        //check se esiste user con email e password che ha il ruolo role
        String query="SELECT * FROM User JOIN ? ON UserId_fk=User.Id WHERE email=? and password=?";
        Connection c=ds.getConnection();
        ResultSet rs;
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1,role);
        ps.setString(2,email);
        ps.setString(3,password);
        rs=ps.executeQuery();
        if(!rs.next()){
            // se non esiste ritorna null
            return null;
        }
        else{
            //se esiste popolo User
            int id= rs.getInt("id");
            String name= rs.getString("name");
            String emailu= rs.getString("email");
            String surname= rs.getString("surname");
            String passwordu= rs.getString("password");
            Set<String> ruoli= new TreeSet<>();

            int authorId = 0, validatorId = 0;

            //check if author
            query="SELECT * FROM User JOIN Author ON Author.UserId_fk=User.Id WHERE email=? and password=?";
            ps = c.prepareStatement(query);
            ps.setString(2,email);
            ps.setString(3,password);
            rs=ps.executeQuery();
            if(rs.next()) {
                ruoli.add("Author");
                authorId = rs.getInt("id");
            }

            // check if validator
            query="SELECT * FROM User JOIN Validator ON Author.UserId_fk=User.Id WHERE email=? and password=?";
            ps = c.prepareStatement(query);
            ps.setString(2,email);
            ps.setString(3,password);
            rs=ps.executeQuery();
            if(rs.next()) {
                ruoli.add("Validator");
                validatorId = rs.getInt("id");
            }

            User u = User.makeUser(id,name,surname,emailu,passwordu,ruoli);
            u.setCurrentRole(role);

            ProposalDAO proposalDAO = new ProposalDAO(ds);

            if(role.equals("Validator")) {
                //CHEK: aggiungere recupero validator
                Set<Proposal> proposals = proposalDAO.findByValidator(validatorId);
                u.setRoleValidator(Validator.makeValidator(validatorId, proposals));
            };
            if(role.equals("Author")) {
                //CHEK: aggiungere recupero author
                Set<Proposal> proposals = proposalDAO.findByAuthor(authorId);
                u.setRoleAuthor(Author.makeAuthor(authorId, proposals, null, null, null));
            };

            return u;
        }
    }
}
