package view;

import java.io.*;
import java.rmi.ServerException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import userManager.User;

@WebServlet(name = "HomeServlet", value = "/home")
public class Home extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String role = "Guest";
        User pippo=(User) request.getSession().getAttribute("user");
        if (pippo!=null && pippo.getCurrentRole() != null)
            role=pippo.getCurrentRole();



        if(role.equals("Author")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/homeAuthor.jsp");
            dispatcher.forward(request, response);
            return;
        }



        if(role.equals("Validator")){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/homeValidator.jsp");
            dispatcher.forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/home.jsp");
    }

    public void destroy() {
    }
}