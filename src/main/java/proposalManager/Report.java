package proposalManager;

import java.io.File;

public class Report {
    public static boolean checkReport(File fileReport, long fileDim){
        if(! fileReport.getAbsolutePath().endsWith(".pdf"))
            return false;

        if(fileDim > MAX_FILE_DIM)
            return false;

        return true;
    }

    private static long MAX_FILE_DIM = 500000000;
}
