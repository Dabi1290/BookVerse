package view.author;

import java.io.*;
import java.sql.SQLException;
import java.util.Set;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import ebookManager.EBook;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import proposalManager.Proposal;
import storageSubSystem.EBookDAO;
import storageSubSystem.ProposalDAO;
import userManager.Author;
import userManager.User;
import userManager.Validator;

import javax.sql.DataSource;

@WebServlet(name = "MyJobsServlet", value = "/MyJobs")
public class MyJobs extends HttpServlet {
    protected static String NEXT_PAGE = "/myJobs.jsp";
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      doPost(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Retrieve user from session
        HttpSession session = (HttpSession) request.getSession();

        User user = (User) session.getAttribute(SessionCostants.USER);
        Author author = user.getRoleAuthor();
        //Retrieve user from session



        //Retrieve data source and build ProposalDAO
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        EBookDAO eBookDAO = new EBookDAO(ds);
        //Retrieve data source and build ProposalDAO

        try {
            Set<EBook> proposals = eBookDAO.findByMainWritten(author.getId());
            author.setWritten(proposals);
            proposals = eBookDAO.findByCoWritten(author.getId());
            author.setCoWritten(proposals);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to retrieve the proposals associated with this main author");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(NEXT_PAGE);
        dispatcher.forward(request, response);

    }

}