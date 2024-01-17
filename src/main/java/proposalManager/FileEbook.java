package proposalManager;

import java.io.File;

public class FileEbook {
    public static boolean checkExtension(File fileEbook){

        if(fileEbook.getAbsolutePath().endsWith(".pdf"))
            return true;
        else
            return false;
    }
}
