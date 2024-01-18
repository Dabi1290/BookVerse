package storageSubSystem;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDAO {

    /**
     *
     * This method create a directory with the path: path + "/" + directoryName.
     *
     * @param path path of the directory where to create file
     * @param directoryName directory's name that is created
     * @throws Exception
     */
    public static void createDirectory(Path path, String directoryName) throws Exception {

        String filePath = path.resolve(directoryName).toString();
        System.out.println(filePath);
        File file = new File(filePath);

        if(file.exists())
            throw new Exception("The directory already exists");

        Files.createDirectory(Path.of(filePath));
    }

    /**
     *
     * This method create a file with content: content and filename: path + "/" + fileName.
     *
     * @param path path of the directory where to create file
     * @param fileName file's name that is created
     * @param content content of file to create
     * @throws Exception
     */
    public static void persistFile(Path path, String fileName, InputStream content) throws Exception {

        String filePath = path.resolve(fileName).toString();
        File file = new File(filePath);

        if (file.exists())
            throw new Exception("The file " + filePath + " already exists");

        Files.copy(content, Path.of(filePath));
    }

    private static String filesDirectory;
    private static boolean alreadySetted = false;

    public static void setFilesDirectory(String rootDirectory) throws Exception {
        if(alreadySetted == true)
            throw new Exception("Root directory of tomcat already setted");

        alreadySetted = true;
        filesDirectory = rootDirectory;
    }

    public static String getFilesDirectory() {
        return filesDirectory;
    }
}
