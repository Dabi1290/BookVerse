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

    //Request parameters
    protected static String EMAIL_PAR = "email";
    protected static String PASSWORD_PAR = "password";
    protected static String ROLE_PAR = "role";
    //Request parameters

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Check for valid parameters
        String email = request.getParameter(EMAIL_PAR);
        if(email == null || email.isEmpty())
            throw new ServletException("email not valid value");

        String password = request.getParameter(PASSWORD_PAR);
        if(password == null || password.isEmpty())
            throw new ServletException("password not valid value");

        String role = request.getParameter(ROLE_PAR);
        if((role == null || role.isEmpty()) || ! (role.equals("Author") || role.equals("Validator")))
            throw new ServletException("role not valid value");
        //Check for valid parameters



        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        UserDAO userDAO = new UserDAO(ds);



        User user;
        try {
            String hashedPassword = login.toHash(password);
            user = userDAO.login(email, hashedPassword, role);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("SQL error");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        //wrong credentials
        if(user == null) {
            request.setAttribute("error", "Wrong credentials");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            return ;
        }

        //right credentials
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

