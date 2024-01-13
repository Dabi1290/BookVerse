package storageSubSystem;

import ebookManager.EBook;
import userManager.Author;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class EBookDAO {
    private DataSource ds = null;

    public EBookDAO(DataSource ds) {
        this.ds = ds;
    }

    public void newEbook(EBook ebook) throws SQLException {

        Connection c = ds.getConnection();

        String title = ebook.getTitle();
        int price = ebook.getPrice();
        String des = ebook.getDescription();
        boolean inCatalog = ebook.isInCatalog();
        String file = ebook.getEbookFile().getName(); //CHECK
        int p_id_fk = ebook.getProposedThrough().getId();
        int mA_id = ebook.getMainAuthor().getId();

        String query = "INSERT INTO EBook (title, price, description, inCatalog, ebookFile, proposalId_fk, mainAuthorId_fk) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = c.prepareStatement(query);

        ps.setString(1, title);
        ps.setInt(2, price);
        ps.setString(3, des);
        ps.setBoolean(4, inCatalog);
        ps.setString(5, file);
        ps.setInt(6, p_id_fk);
        ps.setInt(7, mA_id);

        boolean result = ps.execute();

        if(!result)
            throw new SQLException("Ebook non salvato correttamente!");

        Set<String> generi= ebook.getGenres();
        if(generi != null)
            for(String s : generi) {
                query = "INSERT INTO EBookGenre (ebookId_fk, genreId_fk) VALUES(?, ?)";
                ps = c.prepareStatement(query);
                ps.setInt(1, ebook.getId());
                ps.setString(2, s);
                result = ps.execute();
                if(!result)
                    throw new SQLException("genere non salvato correttamente!");
            }

        Set<Author> coAuthors = ebook.getCoAuthors();

        if(coAuthors != null)
            for(Author a : coAuthors){
                query = "INSERT INTO EBookAuthor (authorId_fk, ebookId_fk) VALUES(?, ?)";
                ps = c.prepareStatement(query);
                ps.setInt(a.getId(), ebook.getId());
                result = ps.execute();
                if(!result)
                    throw new SQLException("coAutore non salvato correttamente!");
            }
    }

}
