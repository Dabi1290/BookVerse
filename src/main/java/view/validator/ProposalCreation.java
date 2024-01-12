package view.validator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "ProposalCreation", value="/ProposalCreation")
public class ProposalCreation extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String title = request.getParameter("title");
        if(title == null || title.isEmpty())
            throw new ServletException("");

        String price_ = request.getParameter("price");
        if(price_ == null || price_.isEmpty())
            throw new ServletException("");
        int price = Integer.parseInt(price_);
        if(price < 0)
            throw new ServletException("");

        Part fileEbookPart = request.getPart("ebookFile");
        if(fileEbookPart == null)
            throw new ServletException("");

        Part fileCoverImage = request.getPart("coverImage");
        if(fileCoverImage == null)
            throw new ServletException("");

        String[] genresParameter = request.getParameterValues("genres");
        if(genresParameter == null || genresParameter.length == 0)
            throw new ServletException("");


    }
}
