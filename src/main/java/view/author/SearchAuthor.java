package view.author;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import storageSubSystem.AuthorDAO;
import userManager.Author;
import userManager.User;

import javax.sql.DataSource;


@WebServlet(name = "SearchAuthorServlet", value = "/SearchAuthor")
public class SearchAuthor extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public SearchAuthor() {
        super();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String query = request.getParameter("query");
        AuthorDAO dao= new AuthorDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        try {
            List<User> authors= (List<User>) dao.findAuthorsByEmail(query);
            Gson gson = new Gson();
            String json = gson.toJson(authors);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }


}