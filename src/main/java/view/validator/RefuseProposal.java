package view.validator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import proposalManager.Proposal;
import proposalManager.Version;
import storageSubSystem.ProposalDAO;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RefuseProposal", value = "/RefuseProposal")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20, // 20MB
        maxFileSize = 1024 * 1024 * 100,      // 100MB
        maxRequestSize = 1024 * 1024 * 500)   // 500MB
public class RefuseProposal extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Retrieve parameters from the request
        Part reportFile = request.getPart("report");
        if(reportFile == null)
            throw new ServletException("Report is not valid");

        Proposal proposal = (Proposal)request.getAttribute("proposal"); //CHECK retrieve the proposal from the session
        if(proposal == null)
            throw new ServletException("Proposal is not valid");
        //Retrieve parameters from the request



        //Retrieve data source from servlet context and create proposal's dao
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        //Retrieve data source from servlet context and create proposal's dao



        //Add report file to the last version of the proposal and update state of the proposal
        Version lastVersion = proposal.lastVersion();

        String tomcatRootDirectory = getServletContext().getRealPath("/");
        int versionId = lastVersion.getId();
        String reportName = "reportFile_" + Integer.toString(versionId) + ".png";

        lastVersion.setReport(new File(tomcatRootDirectory + "/../Files/" + Integer.toString(proposal.getId()) + "/" + reportName));
        proposal.refuse();
        //Add report file to the last version of the proposal and update state of the proposal



        //Persist updates of proposal and lastVersion
        try {
            proposalDao.persistVersion(proposal, lastVersion); //CHECK persist the update of the state of the proposal
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to add the report file to the last version of the proposal");
        }
        //Persist updates of proposal and lastVersion
    }
}
