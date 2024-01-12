package com.bookverse.bookverse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import storageSubSystem.GenreDAO;

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



        GenreDAO genreDao = new GenreDAO(ds);
        List<String> genres = null;
        try {
            genres = genreDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sc.setAttribute("Genres", genres);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code here
    }
}

