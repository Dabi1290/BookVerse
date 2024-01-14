package view.user;

import java.io.*;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import storageSubSystem.UserDAO;
import userManager.User;

import javax.sql.DataSource;

@WebServlet(name = "logoutServlet", value = "/logoutServlet")
public class logout extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    //CHECK add all the message for the exception
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/home");
    }

}