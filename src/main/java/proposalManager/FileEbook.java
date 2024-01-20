package proposalManager;

import java.io.File;

public class FileEbook {
    public static boolean checkFile(File fileEbook, long fileDim){

        if(! fileEbook.getAbsolutePath().endsWith(".pdf"))
            return false;

        if(fileDim > MAX_FILE_DIM)
            return false;

        return true;
    }

    public static boolean checkFile(File fileEbook) {

        if(! fileEbook.getAbsolutePath().endsWith(".pdf"))
            return false;

        return true;
    }

    private static long MAX_FILE_DIM = 500000000;
}
