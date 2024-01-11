package com.bookverse.bookverse;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "PagamentoServlet", value = "/Pagamento-servlet")
public class Pagamento extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.getSession().setAttribute("imagePath","bigWrong.png");
        request.getSession().setAttribute("msg","Transaction failed! Something went wrong");
        response.sendRedirect(request.getContextPath() + "/confirmationPage.jsp");
    }

    public void destroy() {
    }
}