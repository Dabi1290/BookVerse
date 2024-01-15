package com.bookverse.bookverse;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import userManager.User;

import java.io.IOException;

//@WebFilter(filterName = "/AccessControlFiler", urlPatterns = "/*")
public class AccessControlFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        String path = httpServletRequest.getServletPath();

        synchronized (session) {
            //Retrieve user from the session
            User user = (User) session.getAttribute("user");
            //Retrieve user from the session

            

            filterChain.doFilter(request, response);
        }
    }

}
