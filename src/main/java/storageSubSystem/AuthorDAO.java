package storageSubSystem;

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

    public Author findByID(int id) throws SQLException {
        Author a=null;

        String query = "SELECT * FROM Author WHERE id=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1,id);

        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            a = new Author(id);
        }

        return a;
    }

    public Set<User> findAuthorsByEmail(String email) throws SQLException {
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
        return authors;
    }
}
