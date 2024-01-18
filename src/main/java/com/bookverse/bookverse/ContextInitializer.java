package com.bookverse.bookverse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import storageSubSystem.FileDAO;
import storageSubSystem.GenreDAO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebListener
public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();


        String tomcatRootDirectory = sce.getServletContext().getRealPath("/");


        try {
            FileDAO.setFilesDirectory(tomcatRootDirectory + "/../Files/");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        File file = new File(FileDAO.getFilesDirectory());
        System.out.println(FileDAO.getFilesDirectory());
        if(!file.exists() || !file.isDirectory()) {
            boolean r = file.mkdir();
            if(! r)
                throw new RuntimeException("Directory files not created");
        }



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

