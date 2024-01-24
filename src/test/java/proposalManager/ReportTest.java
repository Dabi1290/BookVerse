package proposalManager;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    @Test
    void checkReportE1() {

        File fileReport = new File("report.pdf");

        assertTrue(Report.checkReport(fileReport));

    }

    @Test
    void checkReportE2() {

        File fileReport = new File("report.png");

        assertFalse(Report.checkReport(fileReport));

    }



}