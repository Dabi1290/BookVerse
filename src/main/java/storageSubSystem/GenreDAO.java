package storageSubSystem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

    private DataSource ds = null;

    public GenreDAO(DataSource ds) {
        this.ds = ds;
    }

    public List<String> findAll() throws SQLException {
        List<String> genres = new ArrayList<>();

        String query = "SELECT * FROM Genre";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            String genreName = rs.getString("name");
            genres.add(genreName);
        }

        c.close();

        return genres;
    }
}
