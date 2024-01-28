package com.bookverse.bookverse;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import userManager.User;

import java.io.IOException;
import java.util.*;

@WebFilter(filterName = "/AccessControlFiler", urlPatterns = "/*")
public class AccessControlFilter extends HttpFilter implements Filter {

    //Map servlet/page -> role
    private Map<String, List<String>> servletToRoles;

    private static String ROLE_AUTHOR = "Author";
    private static String ROLE_VALIDATOR = "Validator";
    private static String ROLE_GUEST = "Guest";

    @Override
    public void init(FilterConfig filterConfig) {
        servletToRoles = new HashMap<>();

        //Load association servlet/page -> role
        servletToRoles.put("/home", Arrays.asList(ROLE_AUTHOR, ROLE_VALIDATOR, ROLE_GUEST));


        servletToRoles.put("/login", Arrays.asList(ROLE_GUEST));
        servletToRoles.put("/logout", Arrays.asList(ROLE_VALIDATOR, ROLE_AUTHOR));


        servletToRoles.put("/MyJobs", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/ProposalCorrection", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/ProposalCreation", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/Publications", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/SearchAuthor", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/PayProposal", Arrays.asList(ROLE_AUTHOR));

        servletToRoles.put("/ApproveProposal", Arrays.asList(ROLE_VALIDATOR));
        servletToRoles.put("/Proposals", Arrays.asList(ROLE_VALIDATOR));
        servletToRoles.put("/PermanentlyRefuse", Arrays.asList(ROLE_VALIDATOR));
        servletToRoles.put("/RefuseProposal", Arrays.asList(ROLE_VALIDATOR));

        servletToRoles.put("/FileDownload", Arrays.asList(ROLE_VALIDATOR, ROLE_AUTHOR));

        servletToRoles.put("/confirmationPage.jsp", Arrays.asList(ROLE_VALIDATOR, ROLE_AUTHOR, ROLE_GUEST));
        servletToRoles.put("/correctProposalForm.jsp", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/history", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/historyv", Arrays.asList(ROLE_VALIDATOR));
        servletToRoles.put("/home.jsp", Arrays.asList(ROLE_AUTHOR, ROLE_VALIDATOR, ROLE_GUEST));
        servletToRoles.put("/homeAuthor.jsp", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/homeValidator.jsp", Arrays.asList(ROLE_VALIDATOR));
        servletToRoles.put("/login.jsp", Arrays.asList(ROLE_GUEST));
        servletToRoles.put("/myJobs.jsp", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/payment.jsp", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/proposalForm.jsp", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/register.jsp", Arrays.asList(ROLE_GUEST));
        servletToRoles.put("/proposals.jsp", Arrays.asList(ROLE_VALIDATOR));
        servletToRoles.put("/publications.jsp", Arrays.asList(ROLE_AUTHOR));
        servletToRoles.put("/requisiti.jsp", Arrays.asList(ROLE_AUTHOR));
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
            List<String> roles = servletToRoles.get(path);
            //Retrieve path to the servlet


            //If the page/servlet is not mapped get out!
            if(roles == null) {
                System.out.println("Trying to access to a not mapped functionality");
                httpServletResponse.sendError(500);
                return;
            }
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
