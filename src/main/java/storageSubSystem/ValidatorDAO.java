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

        //Check if parameters are valid
        if(mainAuthor == null || coAuthors == null)
            throw new InvalidParameterException("mainAuthor or coAuthors can't be null");

        if(mainAuthor.getId() <= 0)
            throw new InvalidParameterException("not a valid value for id");

        if( coAuthors.stream().anyMatch(a -> a.getId() <= 0))
            throw new InvalidParameterException("not a valid value for id");

        AuthorDAO authorDAO = new AuthorDAO(ds);

        if(authorDAO.findByID(mainAuthor.getId()) == null)
            throw new InvalidParameterException("This mainAuthor doesn't exist on database");

        for(Author a : coAuthors) {
            if(authorDAO.findByID(a.getId()) == null)
                throw new InvalidParameterException("At least a coAuthor doesn't exist on database");
        }
        //Check if parameters are valid



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

        if (rs.next()) {
            numeroRighe = rs.getInt(1);
        }

        Random rand = new Random();

        int idValidator = mainAuthor.getId();
        while(idAuthors.contains(idValidator)) {
            idValidator = rand.nextInt((numeroRighe - 1) + 1) + 1;
        }

        Validator validator = Validator.makeValidator(idValidator, null);

        c.close();

        return validator;
    }

    public Validator findValidatorById(int validatorId) throws InvalidParameterException, SQLException {

        //Check parameters
        if(validatorId <= 0)
            throw new InvalidParameterException("Value not valid for id");
        //Check parameters


        String query = "SELECT * FROM Validator WHERE id = ?";

        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, validatorId);
        ResultSet rs = ps.executeQuery();

        if(! rs.next()) {
            c.close();
            return null;
        }

        Validator validator = new Validator();
        validator.setId(rs.getInt("id"));

        c.close();

        return validator;
    }
}
