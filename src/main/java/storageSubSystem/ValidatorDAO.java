package storageSubSystem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import userManager.Validator;

public class ValidatorDAO {

    private DataSource ds;

    public ValidatorDAO(DataSource ds) {
        this.ds = ds;
    }
}
