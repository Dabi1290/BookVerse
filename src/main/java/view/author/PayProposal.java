package view.author;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import PaymenManager.Bank;
import PaymenManager.BankAdapter;
import com.bookverse.bookverse.sessionConstants.SessionCostants;
import ebookManager.EBook;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import proposalManager.Proposal;
import proposalManager.WrongStatusException;
import storageSubSystem.EBookDAO;
import storageSubSystem.ProposalDAO;
import userManager.Author;
import userManager.User;
import userManager.Validator;

import javax.sql.DataSource;

@WebServlet(name = "PayProposal", value = "/PayProposal")
public class PayProposal extends HttpServlet {
    protected static String PROPOSALID_PAR = "proposalId";
    protected static String CARD_NUMBER = "cardNumber";
    protected static String CVV = "cvv";
    protected static String MONTH = "month";
    protected static String YEAR = "year";
    protected static String INTESTATARIO = "intestatario";
    protected static String PRICE = "price";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id_ = request.getParameter(PROPOSALID_PAR);
        if(id_ == null || id_.isEmpty())
            throw new ServletException("proposal id is not valid");

        int id = Integer.parseInt(id_);
        if(id <= 0)
            throw new ServletException("proposalId is not valid");

        String cardNumber = request.getParameter(CARD_NUMBER);
        if(cardNumber == null || cardNumber.isEmpty())
            throw new ServletException("card number is not valid");

        String cvv = request.getParameter(CVV);
        if(cvv == null || cvv.isEmpty())
            throw new ServletException("cvv is not valid");

        String month = request.getParameter(MONTH);
        if(month == null || month.isEmpty())
            throw new ServletException("month is not valid");

        String year = request.getParameter(YEAR);
        if(year == null || year.isEmpty())
            throw new ServletException("year is not valid");

        String intestatario = request.getParameter(INTESTATARIO);
        if(intestatario == null || intestatario.isEmpty())
            throw new ServletException("intestatario is not valid");

        String price_ = "50";//request.getParameter(PRICE);
        if(price_ == null || price_.isEmpty())
            throw new ServletException("price is not valid");
        int price = Integer.parseInt(price_);



        //Retrieve localDate
        LocalDate dataScadenza = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 30);
        //Retrieve localDate



        //Retrieve User from the Session
        User user = (User)request.getSession().getAttribute(SessionCostants.USER);
        Author author = user.getRoleAuthor();


        //Retrieve proposal from the session
        Set<Proposal> proposals =author.getProposed();
        Proposal proposal = null;
        for(Proposal p : proposals) {
            if(id == p.getId())
                proposal = p;
        }
        if(proposal == null)
            throw new ServletException("Failed to retrieve proposal from the session");


        //Try to pay the bank
        BankAdapter bank = new BankAdapter();

        //check del formato dati e provo ad effettuare pagamento
        if(! bank.checkDataFormat(cvv, dataScadenza, intestatario, cardNumber, price))
            throw new ServletException("Failed to pay, the parameters isn't correct");

        if(! bank.pay(cvv, dataScadenza, intestatario, cardNumber, price)) {
            response.sendRedirect("/confirmationPage.jsp?imagePath=bigWrong.png&msg=Transaction%20failed%21%20Something%20went%20wrong");
            return;
        }
        //Try to pay the bank



        //Retrieve data source and create ProposalDao
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        //Retrieve data source and create ProposalDao



        //Update status of proposal
        try {
            proposal.pay();
        } catch (WrongStatusException e) {
            e.printStackTrace();
            throw new ServletException("Failed to update status of proposal");
        }

        try {
            proposalDao.updateProposalState(proposal);
        } catch (SQLException e) {
            throw new ServletException("Failed to update status of proposal in DB");
        }
        //Update status of proposal



        //create ebook
        EBook ebook = new EBook();
        ebook = EBook.makeEbook(proposal);

        EBookDAO eBookDAO = new EBookDAO(ds);
        try {
            int ebookId = eBookDAO.newEbook(ebook);
            ebook.setId(ebookId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to add ebook in DB");
        }
        //create ebook

        //CHECK: set ebookId........



        //se tutto è andato bene mostro la confirmation page
        response.sendRedirect("/confirmationPage.jsp?imagePath=bigCheck.png&msg=Your%20publication%20proposal%20has%20been%20successfully%20submitted%2C%20you%20will%20receive%20acknowledgement%20within%2010%20business%20days");
    }
}
