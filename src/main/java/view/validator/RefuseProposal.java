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
import storageSubSystem.AuthorDAO;
import storageSubSystem.BaseFileDAO;
import storageSubSystem.ProposalDAO;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

@WebServlet(name = "RefuseProposal", value = "/RefuseProposal")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20, // 20MB
        maxFileSize = 1024 * 1024 * 100,      // 100MB
        maxRequestSize = 1024 * 1024 * 500)   // 500MB
public class RefuseProposal extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //CHECK controlla che l'author che effettua questa operazione è il mainAuthor

        //Retrieve parameters from the request
        Part reportFile = request.getPart("report");
        if(reportFile == null)
            throw new ServletException("Report is not valid");

        String proposalId_ = request.getParameter("proposalId");
        if(proposalId_ == null || proposalId_.isEmpty())
            throw new ServletException("ProposalId is not valid");
        int proposalId = Integer.parseInt(proposalId_);



        DataSource ds = (DataSource) request.getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        Proposal proposal = null;
        try {
            proposal = proposalDao.findById(proposalId);
            if(proposal.isAlreadyLoadedAuthor()) {
                proposal.setAlreadyLoadedAuthor(true);
                //CARICO AUTHOR
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //Retrieve parameters from the request



        //Add report file to the last version of the proposal and update state of the proposal
        Version lastVersion = proposal.lastVersion();

        String tomcatRootDirectory = getServletContext().getRealPath("/");
        int versionId = lastVersion.getId();
        String reportName = "reportFile_" + Integer.toString(versionId) + ".pdf";

        lastVersion.setReport(new File(tomcatRootDirectory + "/../Files/" + Integer.toString(proposal.getId()) + "/" + reportName));
        proposal.refuse();
        System.out.println(proposal.getStatus());
        //Add report file to the last version of the proposal and update state of the proposal



        //Persist updates of proposal and lastVersion
        try {
            proposalDao.updateVersion(lastVersion); //CHECK persist the update of the state of the proposal
            proposalDao.updateProposalState(proposal);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to add the report file to the last version of the proposal");
        }
        //Persist updates of proposal and lastVersion



        //Save file of report
        try {
            BaseFileDAO.persistFile(Path.of(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/"), reportName, reportFile.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //Save file of report
    }
}
