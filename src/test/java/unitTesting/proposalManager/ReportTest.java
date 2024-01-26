package unitTesting.proposalManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import proposalManager.Report;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    @Test
    void checkReport_E1() {

        File fileReport = new File("report.pdf");

        Assertions.assertTrue(Report.checkReport(fileReport));

    }

    @Test
    void checkReport_E2() {

        File fileReport = new File("report.png");

        assertFalse(Report.checkReport(fileReport));

    }



}