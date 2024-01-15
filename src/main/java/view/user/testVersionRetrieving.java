package view.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import proposalManager.Version;
import storageSubSystem.ProposalDAO;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "testVersionRetrieving", value = "/testVersionRetrieving")
public class testVersionRetrieving extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String proposalId_ = request.getParameter("proposalId");
        int proposalId = Integer.parseInt(proposalId_);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        List<Version> versions;
        ProposalDAO proposalDAO = new ProposalDAO(ds);
        try {
            versions = proposalDAO.findProposalVersions(proposalId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("");
        }

        if(versions != null) {
            for(Version version : versions) {
                System.out.println(version.getDate());

                Set<String> genres = version.getGenres();
                for(String genre : genres) {
                    System.out.println(genre);
                }
            }
        }
    }
}
