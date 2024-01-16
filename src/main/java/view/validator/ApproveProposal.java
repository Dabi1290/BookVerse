package view.validator;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import proposalManager.Proposal;
import storageSubSystem.ProposalDAO;
import userManager.User;
import userManager.Validator;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@WebServlet(name = "ApproveProposal", value = "/ApproveProposal")
public class ApproveProposal extends HttpServlet {

    protected static String PROPOSALID_PAR = "proposalId";
    protected static String NEXT_PAGE = "/proposals.jsp";
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Retrieve parameters from request
        String proposalId_ = request.getParameter(PROPOSALID_PAR);
        if(proposalId_ == null || proposalId_.isEmpty())
            throw new ServletException("");
        int proposalId = Integer.parseInt(proposalId_);
        //Retrieve parameters from request



        //Retrieve user from the session
        HttpSession session = (HttpSession) request.getSession();

        User user = (User)session.getAttribute(SessionCostants.USER);
        Validator validator = user.getRoleValidator();
        //Retrieve user from the session



        //Retrieve proposal from the session
        Proposal proposal = null;
        Set<Proposal> proposals = validator.getAssignedProposals();
        for(Proposal p : proposals) {
            if(p.getId() == proposalId)
                proposal = p;
        }
        if(proposal == null)
            throw new ServletException("Failed to load the specified proposal from the session");
        //Retrieve proposal from the session



        //Retrieve data source and build ProposalDao
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        //Retrieve data source and build ProposalDao


        //Approve proposal and update the state of proposal to database
        try {
            proposal.approve();
            proposalDao.updateProposalState(proposal);
        } catch (SQLException e) {
            throw new ServletException("Failed to update the state of proposal in the database");
        }
        //Approve proposal and update the state of proposal to database



        //Redirect to next page
        response.sendRedirect(NEXT_PAGE);
        //Redirect to next page
    }
}