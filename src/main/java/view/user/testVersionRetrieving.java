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

@WebServlet(name = "testVersionRetrieving", value = "/testVersionRetrieving")
public class testVersionRetrieving extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String proposalId_ = request.getParameter("proposalId");
        int proposalId = Integer.parseInt(proposalId_);

        System.out.println(proposalId);

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
                System.out.println(version.getId());
            }
        }
    }

}
