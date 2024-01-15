package view.validator;

import java.io.*;
import java.sql.SQLException;
import java.util.Set;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){

        //suppongo che davide mi passerà l'id, dato che il tasto di rifiuto permanente è uno per ogni proposta...

        int id = 0;

        id = Integer.parseInt(request.getParameter("id"));

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


        //Retrieve proposal from the cache
        Set<Proposal> proposals =validator.getAssignedProposals();
        Proposal proposal = null;
        for(Proposal p : proposals) {
            if(proposal.getId() == p.getId())
                proposal = p;
        }

        //rifiuto permanentemente la proposta (se l'ho recuperata)
        if(proposal != null)
            proposal.permanentlyRefuse();

        //aggiorno lo stato sul DB
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        try {
            if(proposal != null)
                proposalDao.updateProposalState(proposal);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}