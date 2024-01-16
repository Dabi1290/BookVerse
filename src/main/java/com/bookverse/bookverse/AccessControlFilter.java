package com.bookverse.bookverse;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import userManager.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebFilter(filterName = "/AccessControlFiler", urlPatterns = "/*")
public class AccessControlFilter extends HttpFilter implements Filter {

    //Map servlet/page -> role
    private Map<String, List<String>> pageToRoles;

    @Override
    public void init(FilterConfig filterConfig) {
        pageToRoles = new HashMap<>();

        //Load association servlet/page -> role
        pageToRoles.put("/homeAuthor.jsp", Arrays.asList("Author"));
        pageToRoles.put("/homeValidator.jsp", Arrays.asList("Validator"));
        //Load association servlet/page -> role
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        String path = httpServletRequest.getServletPath();

        //Free access for the assets
        if(path.contains("/assets/")) {
            filterChain.doFilter(request, response);
            return;
        }
        //Free access for the assets

        synchronized (session) {
            //Retrieve user from the session
            User user = (User) session.getAttribute("user");
            String currentRole = "Guest";
            if(user != null)
                currentRole = user.getCurrentRole();
            //Retrieve user from the session


            //Retrieve path to the servlet
            List<String> roles = pageToRoles.get(path);
            //Retrieve path to the servlet


            //If the page/servlet is not mapped get out!
//            if(roles == null) {
//                System.out.println("Trying to access to a not mapped ")
//                httpServletResponse.sendError(500);
//                return;
//            }
            //If the page/servlet is not mapped get out!

            //If the page/servlet mapped, control the roles and the right
            if(roles != null) {
                if(! roles.contains(currentRole)) {
                    System.out.println("You don't have permission to access to this functionality");
                    httpServletResponse.sendError(500);
                    return;
                }
            }
            //If the page/servlet mapped, control the roles and the right

            filterChain.doFilter(request, response);
        }
    }

}
