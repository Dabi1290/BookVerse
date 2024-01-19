package view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import storageSubSystem.InvalidParameterException;
import storageSubSystem.ValidatorDAO;

import javax.sql.DataSource;
import java.sql.SQLException;

@WebServlet(name = "testServlet", value = "/testServlet")
public class testServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        int id = Integer.parseInt(request.getParameter("id"));

        DataSource ds = (DataSource)getServletContext().getAttribute("DataSource");
        ValidatorDAO validatorDAO = new ValidatorDAO(ds);

        try {
            if(validatorDAO.findValidatorById(id) == null)
                System.out.println("PORCODIO");
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }

    }

}
