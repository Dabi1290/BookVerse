package com.bookverse.bookverse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebListener
public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        List<String> myList = Arrays.asList("Horror", "Fantasy", "Action","Kids",
                                            "Cook", "Adventure","Politics", "Languages",
                                            "Crime","Thriller", "Sci-Fi", "Society",
                                            "History","Archaeology", "Law", "Biography");
        sc.setAttribute("Genres", myList);

        DataSource ds = null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            ds = (DataSource) envCtx.lookup("jdbc/storage");

            try {
                Connection con = ds.getConnection();

            } catch (SQLException e) {
                System.out.println(e);
            }

        } catch (NamingException e) {
            System.out.println(e);
        }

        sc.setAttribute("DataSource", ds);

        if(ds == null)
            System.out.println("ORCODIO");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code here
    }
}

