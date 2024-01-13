package view.validator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import proposalManager.Proposal;

import java.io.IOException;

@WebServlet(name = "RefuseProposal", value = "/RefuseProposal")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20, // 20MB
        maxFileSize = 1024 * 1024 * 100,      // 100MB
        maxRequestSize = 1024 * 1024 * 500)
public class RefuseProposal extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Part reportFile = request.getPart("report");
        if(reportFile == null)
            throw new ServletException("Report is not valid");

        Proposal proposal = (Proposal)request.getAttribute("proposal");

    }
}
