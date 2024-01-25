package testing;

import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import storageSubSystem.FileDAO;

import javax.sql.DataSource;

@WebServlet(name = "DbResetForFunctionalTestingServlet", value = "/TestingTesting")
public class DbResetForFunctionalTesting extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Path sourceDirectory = Paths.get("/Users/davideamoruso/Documents/BookVerse/src/test/files/defaults/");
        Path targetDirectory = Paths.get(FileDAO.getFilesDirectory());

        if (Files.exists(targetDirectory)) {
            File directory = new File(targetDirectory.toString());
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    }
                }
            }
        } else {
            System.out.println("Directory does not exist.");
        }







        try {
            Files.walk(sourceDirectory)
                    .forEach(source -> {
                        try {
                            Path destination = targetDirectory.resolve(sourceDirectory.relativize(source));
                            if (Files.isDirectory(source)) {
                                if (!Files.exists(destination)) {
                                    Files.createDirectory(destination);
                                }
                            } else {
                                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataSource ds = (DataSource) request.getServletContext().getAttribute("DataSource");
        try {
            Connection c = ds.getConnection();
            SQLScript.executeSQLScript("/Users/davideamoruso/Documents/BookVerse/db/createDb.sql",c);
            SQLScript.executeSQLScript("/Users/davideamoruso/Documents/BookVerse/src/test/db/FunctionalTest/correctProposal.sql",c);
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

        public static void deleteDirectory(File directory) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    }
                    boolean result = file.delete();
                    if (!result) {
                        System.out.println("Failed to delete " + file.getName());
                    }
                }
            }
}}