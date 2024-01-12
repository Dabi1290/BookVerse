package view.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import storageSubSystem.UserDAO;
import userManager.User;

import javax.sql.DataSource;
import java.sql.SQLException;

@WebServlet(name = "login", value = "/login")
public class login extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doPost(request, response);
    }

    //CHECK add all the message for the exception
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String email = request.getParameter("email");
        if(email == null || email.isEmpty())
            throw new ServletException("a");

        String password = request.getParameter("password");
        if(password == null || password.isEmpty())
            throw new ServletException("b");

        String role = request.getParameter("role"); //CHECK add a parameter to indicate the role for the login
        if((role == null || role.isEmpty()) || (role.equals("Author") && role.equals("Validator")))
            throw new ServletException("c");

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        UserDAO userDAO = new UserDAO(ds);

        User user;
        try {
            user = userDAO.login(email, password, role);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("d");
        }

        System.out.println(user.getEmail());

    }
}
