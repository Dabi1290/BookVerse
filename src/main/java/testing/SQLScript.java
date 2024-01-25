package testing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLScript {

    public static String[] retrieveStatements(String filePath) {

        String scriptFile = readScriptContent(filePath);

        String[] statements = scriptFile.split(";");

        return statements;
    }

    private static String readScriptContent(String scriptFilePath) {
        StringBuilder scriptContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scriptContent.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scriptContent.toString();
    }

    public static void executeSQLScript(String scriptFilePath, Connection conn) throws SQLException {
        String[] sqlStatements = retrieveStatements(scriptFilePath);

        // Create a Statement
        try (Statement statement = conn.createStatement()) {
            // Execute each SQL statement
            for (String sql : sqlStatements) {
                // Skip empty statements
                if (!sql.trim().isEmpty()) {
                    statement.execute(sql.trim());
                }
            }
        }
    }
}
