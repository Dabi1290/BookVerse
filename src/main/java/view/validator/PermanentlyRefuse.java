package view.validator;

import java.io.*;
import java.sql.SQLException;
import java.util.Set;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import proposalManager.Proposal;
import storageSubSystem.ProposalDAO;
import userManager.Author;
import userManager.User;
import userManager.Validator;

import javax.sql.DataSource;

@WebServlet(name = "PermanentlyRefuseServlet", value = "/PermanentlyRefuseServlet")
public class PermanentlyRefuse extends HttpServlet {

    protected static String PROPOSALID_PAR = "proposalId";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String id_ = request.getParameter(PROPOSALID_PAR);
        if(id_ == null || id_.isEmpty())
            throw new ServletException("proposal id is not valid");

        int id = Integer.parseInt(id_);

        /*
        //carico la proposta con quell'ID
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);

        Proposal proposal = null;

        try {
            if(id != 0) // difatti id non può essere 0, gli id del DB partono da 1 !!
                proposal = proposalDao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

         */

        //Retrieve User from the Session
        User user = (User)request.getSession().getAttribute(SessionCostants.USER);
        Validator validator = user.getRoleValidator();
        //Retrieve User from the Session



        //Retrieve proposal from the session
        Set<Proposal> proposals =validator.getAssignedProposals();
        Proposal proposal = null;
        for(Proposal p : proposals) {
            if(proposal.getId() == p.getId())
                proposal = p;
        }
        if(proposal == null)
            throw new ServletException("Failed to retrieve proposal from the session");
        //Retrieve proposal from the session



        //Update state of proposal on database
        proposal.permanentlyRefuse();
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        try {
            proposalDao.updateProposalState(proposal);
        } catch (SQLException e) {
            throw new ServletException("Failed to update state of proposal on database");
        }
        //Update state of proposal on database
    }

}