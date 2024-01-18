package storageSubSystem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import proposalManager.Proposal;
import userManager.Author;
import userManager.Validator;
import userManager.ValidatorDispatcher;

public class ValidatorDAO implements ValidatorDispatcher {

    private DataSource ds=null;

    public ValidatorDAO(DataSource ds) {
        this.ds = ds;
    }

    public Validator findFreeValidator(Author mainAuthor, Set<Author> coAuthors) throws Exception {

        Set<Integer> idAuthors = new TreeSet<>();
        idAuthors.add(mainAuthor.getId());
        for(Author coAuthor : coAuthors) {
            idAuthors.add(coAuthor.getId());
        }

        Connection c = ds.getConnection();

        String query = "SELECT COUNT(*) FROM Validator";

        PreparedStatement ps = c.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        int numeroRighe = 0;

        if (rs.next())
            numeroRighe = rs.getInt(1);

        Random rand = new Random();

        int idValidator = mainAuthor.getId();
        while(idAuthors.contains(idValidator)) {
            idValidator = rand.nextInt((numeroRighe - 1) + 1) + 1;
        }

        Validator validator = Validator.makeValidator(idValidator, null);

        return validator;

    }
}
