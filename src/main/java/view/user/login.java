package view.user;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import storageSubSystem.UserDAO;
import userManager.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@WebServlet(name = "login", value = "/login")
public class login extends HttpServlet {

    //TO REMOVE
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    //CHECK add all the message for the exception
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Check for valid parameters
        String email = request.getParameter("email");
        if(email == null || email.isEmpty())
            throw new ServletException("a");

        String password = request.getParameter("password");
        if(password == null || password.isEmpty())
            throw new ServletException("b");

        String role = request.getParameter("role"); //CHECK add a parameter to indicate the role for the login
        if((role == null || role.isEmpty()) || ! (role.equals("Author") || role.equals("Validator")))
            throw new ServletException("c");
        //Check for valid parameters



        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        UserDAO userDAO = new UserDAO(ds);



        User user;
        try {
            user = userDAO.login(email, login.toHash(password), role);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("d");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        // credenziali errate
        if(user == null) {
            request.setAttribute("error", "Credenziali Errate");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            return ;
        }

        //credenziali giuste
        HttpSession session = (HttpSession) request.getSession();
        session.setAttribute("user", user);



        response.sendRedirect(request.getContextPath() + "/home");
    }

    public static String toHash(String password) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();

        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
        if(password != null) {
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toHexString(
                        (hash[i] & 0xFF) | 0x100
                ).toLowerCase().substring(1,3));
            }
        }

        return sb.toString();
    }
}

