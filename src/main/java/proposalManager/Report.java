package proposalManager;

import java.io.File;

public class Report {
    public Report(){
    }

    public static boolean checkReport(File fileReport){

        if(fileReport.getAbsolutePath().endsWith(".pdf"))
            return true;
        else
            return false;
    }
}
