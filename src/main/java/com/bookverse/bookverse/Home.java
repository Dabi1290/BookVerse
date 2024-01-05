package com.bookverse.bookverse;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "HomeServlet", value = "/home")
public class Home extends HttpServlet {
    private String role;

    public void init() {
        role = "Validator";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pippo=(String) request.getSession().getAttribute("currentRole");
        if (pippo!=null)role=pippo;
        else request.getSession().setAttribute("currentRole",role);

        if(role.equals("Author")){
            response.sendRedirect(request.getContextPath() + "/homeAuthor.jsp");
            return;
        }

        if(role.equals("Validator")){
            response.sendRedirect(request.getContextPath() + "/homeValidator.jsp");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/home.jsp");
        return;



    }

    public void destroy() {
    }
}