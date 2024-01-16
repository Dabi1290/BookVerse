package view.validator;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import proposalManager.Proposal;
import proposalManager.Version;
import storageSubSystem.AuthorDAO;
import storageSubSystem.BaseFileDAO;
import storageSubSystem.ProposalDAO;
import userManager.User;
import userManager.Validator;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Set;

@WebServlet(name = "RefuseProposal", value = "/RefuseProposal")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20, // 20MB
        maxFileSize = 1024 * 1024 * 100,      // 100MB
        maxRequestSize = 1024 * 1024 * 500)   // 500MB
public class RefuseProposal extends HttpServlet {

    //Request parameters
    protected static String REPORT_PAR = "report";
    protected static String PROPOSALID_PAR = "proposalId";
    protected static String NEXT_PAGE = "/proposals.jsp";
    //Request parameters

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Retrieve parameters from the request
        Part reportFile = request.getPart(REPORT_PAR);
        if(reportFile == null)
            throw new ServletException("Report is not valid");

        String proposalId_ = request.getParameter(PROPOSALID_PAR);
        if(proposalId_ == null || proposalId_.isEmpty())
            throw new ServletException("ProposalId is not valid");
        int proposalId = Integer.parseInt(proposalId_);
        //Retrieve parameters from the request



        //Retrieve user from the session
        HttpSession session = (HttpSession) request.getSession();

        User user = (User) session.getAttribute(SessionCostants.USER);
        Validator validator = user.getRoleValidator();
        //Retrieve user from the session


        //Retrieve proposal with id from the session
        Set<Proposal> assignedProposals = validator.getAssignedProposals();

        Proposal proposal = null;
        for(Proposal assignedProposal : assignedProposals) {
            if(assignedProposal.getId() == proposalId)
                proposal = assignedProposal;
        }

        if(proposal == null)
            throw new ServletException("This proposal is not assigned to this validator");
        //Retrieve proposal with id from the session



        //Retrieve data source and ProposalDAO
        DataSource ds = (DataSource) request.getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        //Retrieve data source and ProposalDAO




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
        response.sendRedirect(NEXT_PAGE);
    }
}
