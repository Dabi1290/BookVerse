package testing;

import java.io.*;
import java.nio.file.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import storageSubSystem.FileDAO;

@WebServlet(name = "DbResetForFunctionalTestingServlet", value = "/TestingTesting")
public class DbResetForFunctionalTesting extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Path sourceDirectory = Paths.get("/Users/davideamoruso/Documents/BookVerse/src/test/files/defaults/");
        Path targetDirectory = Paths.get(FileDAO.getFilesDirectory());

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDirectory)) {
            for (Path path : directoryStream) {
                Path targetPath = targetDirectory.resolve(sourceDirectory.relativize(path));
                Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}