package com.bookverse.bookverse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Arrays;
import java.util.List;

@WebListener
public class GenresListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        List<String> myList = Arrays.asList("Horror", "Fantasy", "Action","Kids",
                                            "Cook", "Adventure","Politics", "Languages",
                                            "Crime","Thriller", "Sci-Fi", "Society",
                                            "History","Archaeology", "Law", "Biography");
        sc.setAttribute("Genres", myList);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code here
    }
}

