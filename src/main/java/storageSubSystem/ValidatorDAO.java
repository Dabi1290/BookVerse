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
    private AuthorDAO authorDao=null;

    public ValidatorDAO(DataSource ds) {
        this.ds = ds;
    }
    public ValidatorDAO(DataSource ds, AuthorDAO authorDao) {
        this.ds = ds;
        this.authorDao = authorDao;
    }

    public Validator findFreeValidator(Author mainAuthor, Set<Author> coAuthors) throws Exception {

        //Check if parameters are valid
        if(mainAuthor == null || coAuthors == null)
            throw new InvalidParameterException("mainAuthor or coAuthors can't be null");

        if(mainAuthor.getId() <= 0)
            throw new InvalidParameterException("not a valid value for id");

        if( coAuthors.stream().anyMatch(a -> a.getId() <= 0))
            throw new InvalidParameterException("not a valid value for id");


        if(authorDao.findByID(mainAuthor.getId()) == null)
            throw new InvalidParameterException("This mainAuthor doesn't exist on database");

        for(Author a : coAuthors) {
            if(authorDao.findByID(a.getId()) == null)
                throw new InvalidParameterException("At least a coAuthor doesn't exist on database");
        }
        //Check if parameters are valid



        Set<Integer> idAuthors = new TreeSet<>();
        idAuthors.add(mainAuthor.getId());
        for(Author coAuthor : coAuthors) {
            idAuthors.add(coAuthor.getId());
        }


        //Format how necessary query
        String notInPart = "(";
        int count = 1;
        for(int id : idAuthors) {

            notInPart += "?";

            if(count != idAuthors.size())
                notInPart += ",";

            count++;
        }
        notInPart += ")";

        String query = "SELECT V.id, Count(ProposalValidator.proposalId_fk) as numProp FROM Validator as V LEFT JOIN ProposalValidator on V.id = ProposalValidator.validatorId_fk WHERE V.id NOT IN "
                + notInPart +
                " GROUP BY V.id ORDER BY numProp LIMIT 1";
        //Format how necessary query



        //Prepare query
        Connection c = ds.getConnection();

        PreparedStatement ps = c.prepareStatement(query);

        int index = 1;
        for(int id : idAuthors) {
            ps.setInt(index, id);
            index++;
        }
        //Prepare query



        //Execute query
        ResultSet rs = ps.executeQuery();

        if(! rs.next()) {
            return null;
        }

        int idValidator = rs.getInt("V.id");
        Validator validator = Validator.makeValidator(idValidator, null);
        //Execute query

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
