package com.bookverse.bookverse;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import proposalManager.Proposal;
import storageSubSystem.AuthorDAO;
import storageSubSystem.ProposalDAO;
import userManager.Author;
import userManager.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@WebServlet(name = "Pubblications", value = "/Publications")
public class Publications extends HttpServlet {

    protected static String NEXT_PAGE = "/publications.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Retrieve author from the session
        HttpSession session = (HttpSession) request.getSession();

        User user = (User) session.getAttribute(SessionCostants.USER);
        Author author = user.getRoleAuthor();
        //Retrieve author from the session



        //Retrieve data source and build proposal dao
        DataSource ds = (DataSource) request.getServletContext().getAttribute("DataSource");

        ProposalDAO proposalDAO = new ProposalDAO(ds);
        //Retrieve data source and build proposal dao



        //Retrieve proposals where author that makes request is main author
        try {
            author.setProposed(proposalDAO.findByMainAuthor(author.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to retrieve the proposals associated with this main author");
        }
        //Retrieve proposals where author that makes request is main author



        //Retrieve proposals where author that make request is a collaborator
        AuthorDAO authorDao = new AuthorDAO(ds);

        try {
            Set<Proposal> proposals = proposalDAO.findByCoAuthor(author.getId());
            for(Proposal proposal : proposals) {
                proposal.setCollaborators(authorDao.findCoAuthorsForProposal(proposal));
                proposal.setProposedBy(authorDao.findMainAuthorForProposal(proposal));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to retrieve the proposals associated with this coAuthor");
        }
        //Retrieve proposals where author that make request is a collaborator



        //Redirect to proposals page
        RequestDispatcher dispatcher = request.getRequestDispatcher(NEXT_PAGE);
        dispatcher.forward(request, response);
        //Redirect to proposals page
    }
}
