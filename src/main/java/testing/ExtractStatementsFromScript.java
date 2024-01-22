package testing;

import java.io.BufferedReader;
import java.io.FileReader;

public class ExtractStatementsFromScript {

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
}
