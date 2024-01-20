package view.validator;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import storageSubSystem.AuthorDAO;
import storageSubSystem.InvalidParameterException;
import storageSubSystem.ProposalDAO;
import storageSubSystem.ValidatorDAO;
import userManager.User;
import userManager.Validator;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Proposals", value="/Proposals")
public class Proposals extends HttpServlet {
    protected static String NEXT_PAGE = "/proposals.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Retrieve user from session
        HttpSession session = (HttpSession) request.getSession();

        User user = (User) session.getAttribute(SessionCostants.USER);
        Validator validator = user.getRoleValidator();
        //Retrieve user from session



        //Retrieve data source and build ProposalDAO
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds, new ValidatorDAO(ds), new AuthorDAO(ds));
        //Retrieve data source and build ProposalDAO



        //Retrieve assigned proposals to validator
        try {
            validator.setAssignedProposals(proposalDao.findByValidator(validator.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to load assigned proposal to this validator");
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
        //Retrieve assigned proposals to validator



        //Forward request and response to next page
        RequestDispatcher dispatcher = request.getRequestDispatcher(NEXT_PAGE);
        dispatcher.forward(request, response);
        //Forward request and response to next page
    }
}
