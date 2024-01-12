package storageSubSystem;

import userManager.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

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
        if(rs.next()) a = new Author(id);
        return a;
    }

}
