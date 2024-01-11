package storageSubSystem;

import userManager.Author;

import javax.sql.DataSource;
import java.util.Collection;

public class AuthorDAO {
    private DataSource ds=null;
    public AuthorDAO(DataSource ds) {
        this.ds=ds;
    }
    /*
    private int id;
    private int userId;
    private Collection<Object> collaboratedTo;
    private Collection<Object> proposed;
    private Collection<Object> written;
    private Collection<Object> coWritten;*/

    public Author findByID(int id){
        //create
        Author a = new Author();
        //id
        a.setId(id);
        //collaboratedTo




        return a;
    }
}
