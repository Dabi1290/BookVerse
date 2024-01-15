package com.bookverse.bookverse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import storageSubSystem.AuthorDAO;
import userManager.User;

import javax.sql.DataSource;

@WebServlet(name = "SearchAuthorServlet", value = "/SearchAuthor")
public class SearchAuthor extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        AuthorDAO dao = new AuthorDAO((DataSource)request.getServletContext().getAttribute("DataSource"));

        try {
            Set<User> authors = dao.findAuthorsByEmail(query);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(authors);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (SQLException var9) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}