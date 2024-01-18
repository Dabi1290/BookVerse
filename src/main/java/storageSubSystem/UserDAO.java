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

    public User login(String email, String password, String role) throws SQLException, Exception {

        //Check for valid parameters
        if (email == null || email.isEmpty())
            throw new Exception("Not supported value for email");

        if (password == null || password.isEmpty())
            throw new Exception("Not supported value for passowrd");

        if( role == null || ! (role.equals("Validator") || role.equals("Author")) )
            throw new Exception("Not supported value for role");
        //Check for valid parameters



        String query = "SELECT * FROM User, " + role + " WHERE email=? and password=? and "+role+".id=User.id";

        Connection c=ds.getConnection();

        ResultSet rs;
        PreparedStatement ps = c.prepareStatement(query);

        ps.setString(1,email);
        ps.setString(2,password);

        rs=ps.executeQuery();

        if(!rs.next()){
            // if not exist return null
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
            query="SELECT * FROM User JOIN Author ON Author.id=User.Id WHERE email=? and password=?";
            ps = c.prepareStatement(query);
            ps.setString(1,email);
            ps.setString(2,password);
            rs=ps.executeQuery();
            if(rs.next()) {
                ruoli.add("Author");
                authorId = rs.getInt("id");
            }

            // check if validator
            query="SELECT * FROM User JOIN Validator ON Validator.id=User.Id WHERE email=? and password=?";
            ps = c.prepareStatement(query);
            ps.setString(1,email);
            ps.setString(2,password);
            rs=ps.executeQuery();
            if(rs.next()) {
                ruoli.add("Validator");
                validatorId = rs.getInt("id");
            }

            User u = User.makeUser(id,name,surname,emailu,passwordu);
            u.setCurrentRole(role);

            ProposalDAO proposalDAO = new ProposalDAO(ds);

            if(role.equals("Validator")) {
                u.setRoleValidator(Validator.makeValidator(validatorId, null));
            };
            if(role.equals("Author")) {
                u.setRoleAuthor(Author.makeAuthor(authorId, null, null, null, null));
            };

            c.close();

            return u;
        }
    }
}
