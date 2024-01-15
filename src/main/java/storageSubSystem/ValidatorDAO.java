package storageSubSystem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import userManager.Validator;

public class ValidatorDAO {

    private DataSource ds=null;

    public ValidatorDAO(DataSource ds) {
        this.ds = ds;
    }

    public Validator findFreeValidator() throws SQLException {

        Connection c = ds.getConnection();

        String query = "SELECT COUNT(*) FROM Validator";

        PreparedStatement ps = c.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        int numeroRighe = 0;

        if (rs.next())
            numeroRighe = rs.getInt(1);

        Random rand = new Random();
        int idValidator = rand.nextInt((numeroRighe - 1) + 1) + 1;

        Validator validator = Validator.makeValidator(idValidator, null);

        return validator;

    }


}
