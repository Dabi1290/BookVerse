package view.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "login", value = "/login")
public class login extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String email = request.getParameter("email");
        if(email == null || email.isEmpty())
            throw new ServletException("");

        String password = request.getParameter("password");
        if(password == null || password.isEmpty())
            throw new ServletException("");


    }
}
