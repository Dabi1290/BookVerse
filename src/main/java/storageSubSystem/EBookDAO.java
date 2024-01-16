package storageSubSystem;

import ebookManager.EBook;
import proposalManager.Proposal;
import userManager.Author;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class EBookDAO {
    private DataSource ds = null;

    public EBookDAO(DataSource ds) {
        this.ds = ds;
    }

    public int newEbook(EBook ebook) throws SQLException {

        Connection c = ds.getConnection();

        String title = ebook.getTitle();
        int price = ebook.getPrice();
        String des = ebook.getDescription();
        boolean inCatalog = ebook.isInCatalog();
        String file = ebook.getEbookFile().getName(); //CHECK
        int p_id_fk = ebook.getProposedThrough().getId();
        int mA_id = ebook.getMainAuthor().getId();

        String query = "INSERT INTO EBook (title, price, description, inCatalog, ebookFile, proposalId_fk, mainAuthorId_fk) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, title);
        ps.setInt(2, price);
        ps.setString(3, des);
        ps.setBoolean(4, inCatalog);
        ps.setString(5, file);
        ps.setInt(6, p_id_fk);
        ps.setInt(7, mA_id);

        //CHECK attention, i rember that this give errors where there isn't
        ps.execute();

        ResultSet rs = ps.getGeneratedKeys();
        if(! rs.next())
            throw new SQLException("Not added ebook");
        int ebookId = rs.getInt(1);



        Set<String> genres= ebook.getGenres();
        if(genres != null)
            for(String s : genres) {
                query = "INSERT INTO EBookGenre (ebookId_fk, genreId_fk) VALUES(?, ?)";
                ps = c.prepareStatement(query);
                ps.setInt(1, ebookId);
                ps.setString(2, s);
                int result = ps.executeUpdate();
                if(result == 0) //CHECK quello di sopra
                    throw new SQLException("Genere ebook non settato");
            }

        Set<Author> coAuthors = ebook.getCoAuthors();

        if(coAuthors != null)
            for(Author a : coAuthors) {
                query = "INSERT INTO EBookAuthor (authorId_fk, ebookId_fk) VALUES(?, ?)";
                ps = c.prepareStatement(query);
                ps.setInt(1, a.getId());
                ps.setInt(2, ebookId);
                int result = ps.executeUpdate();
                if(result == 0) //CHECK quello di sopra
                    throw new SQLException("coAutore non salvato correttamente!");
            }

        c.close();

        return ebookId;
    }
    public Set<EBook> findByCoWritten(int authorId) throws SQLException {

        String query = "SELECT * FROM EBook JOIN EBookAuthor as p ON ebookId_fk=id WHERE p.authorId_fk=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, authorId);

        ResultSet rs = ps.executeQuery();
        Set<EBook> s = new HashSet<>();
        while(rs.next()) {
            EBook p = new EBook();

            p.setId(rs.getInt("id"));
            p.setTitle(rs.getString("title"));
            p.setDescription(rs.getString("description"));
            p.setInCatalog(rs.getBoolean("inCatalog"));

            s.add(p);
        }

        c.close();

        return s;
    }

    public Set<EBook> findByMainWritten(int mainAuthorId) throws SQLException {

        String query = "SELECT * FROM EBook WHERE mainAuthorId_fk=?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, mainAuthorId);
        ResultSet rs = ps.executeQuery();

        Set<EBook> proposals = new HashSet<>();

        while(rs.next()) {
            EBook p = new EBook();

            p.setId(rs.getInt("id"));
            p.setTitle(rs.getString("title"));
            p.setDescription(rs.getString("description"));
            p.setInCatalog(rs.getBoolean("inCatalog"));

            proposals.add(p);
        }

        c.close();

        return proposals;
    }

}
