package com.bookverse.bookverse;

import java.io.*;
import java.rmi.ServerException;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import userManager.User;

@WebServlet(name = "HomeServlet", value = "/home")
public class Home extends HttpServlet {
    private String role;

    public void init() {
        role = "Guest";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User pippo=(User) request.getSession().getAttribute("user");
        if (pippo!=null && pippo.getCurrentRole() != null)
            role=pippo.getCurrentRole();



        if(role.equals("Author")){
            response.sendRedirect(request.getContextPath() + "/homeAuthor.jsp");
            return;
        }



        if(role.equals("Validator")){
            response.sendRedirect(request.getContextPath() + "/homeValidator.jsp");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/home.jsp");
    }

    public void destroy() {
    }
}