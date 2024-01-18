package view.author;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import proposalManager.Proposal;
import proposalManager.Version;
import storageSubSystem.AuthorDAO;
import storageSubSystem.FileDAO;
import storageSubSystem.ProposalDAO;
import storageSubSystem.ValidatorDAO;
import userManager.Author;
import userManager.User;
import userManager.Validator;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


@WebServlet(name = "ProposalCreation", value="/ProposalCreation")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20, // 20MB
        maxFileSize = 1024 * 1024 * 100,      // 100MB
        maxRequestSize = 1024 * 1024 * 500)   // 500MB
public class ProposalCreation extends HttpServlet {

    protected static String TITLE_PAR = "title";
    protected static String PRICE_PAR = "price";
    protected static String DESCRIPTION_PAR = "description";
    protected static String EBOOKFILE_PAR = "ebookFile";
    protected static String COVERIMAGE_PAR = "coverImage";
    protected static String GENRES_PAR = "genres";
    protected static String AUTHORS_PAR = "authors";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Retrieve parameters from request
        String title = request.getParameter(TITLE_PAR);
        if(title == null || title.isEmpty())
            throw new ServletException("title not valid");

        String price_ = request.getParameter(PRICE_PAR);
        if(price_ == null || price_.isEmpty())
            throw new ServletException("price not valid");
        int price = Integer.parseInt(price_);
        if(price < 0)
            throw new ServletException("price not valid value");

        String description = request.getParameter(DESCRIPTION_PAR);
        if(description == null || description.isEmpty())
            throw new ServletException("description not valid");

        Part fileEbookPart = request.getPart(EBOOKFILE_PAR);
        if(fileEbookPart == null)
            throw new ServletException("ebookFile not valid");

        Part fileCoverImage = request.getPart(COVERIMAGE_PAR);
        if(fileCoverImage == null)
            throw new ServletException("coverImage not valid");

        String[] genresParameter = request.getParameterValues(GENRES_PAR);
        if(genresParameter == null || genresParameter.length == 0)
            throw new ServletException("genres not valid");

        Set<String> genres = new TreeSet<>(Arrays.asList(genresParameter));

        //CHECK probably you can pass directly the list of authors
        String[] authors = request.getParameterValues(AUTHORS_PAR);
        //Retrieve parameters from request



        //Retrieve session and user from it
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute(SessionCostants.USER);
        Author mainAuthor = user.getRoleAuthor();
        //Retrieve session and user from it



        //Retrieve data source and build author dao
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        AuthorDAO authorDao = new AuthorDAO(ds);
        //Retrieve data source and build author dao



        //CHECK probably you can pass directly the list of authors
        Set<Author> coAuthors = new HashSet<>();

        if(authors != null)
        for(String authorId_ : authors) {
            try {
                Author coAuthor = authorDao.findByID(Integer.parseInt(authorId_));
                coAuthors.add(coAuthor);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }



        //Create new proposal and persist to database
        ProposalDAO proposalDao = new ProposalDAO(ds);
        Proposal proposal = Proposal.makeProposal(mainAuthor, coAuthors,"pending");
        int proposalId = -1;
        try {
            proposalId = proposalDao.newProposal(proposal);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        proposal.setId(proposalId);
        //Create new proposal and persist to database



        //Find a validator and assign to proposal
        ValidatorDAO validatorDao = new ValidatorDAO(ds);

        try {
            Validator validator = validatorDao.findFreeValidator(proposal.getProposedBy(), proposal.getCollaborators());
            //Validator validator = Validator.makeValidator(2, null);
            proposal.assignValidator(validator);
            proposalDao.assignValidator(proposal, validator);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to assign a validator to this proposal");
        }
        //Find a validator and assign to proposal



        //Create first version of the proposal and persist to database
        Version version = Version.makeVersion(title, description, price, null, null, null, LocalDate.now(),  genres);
        proposal.addVersion(version);

        int versionId = -1;
        try {
            versionId = proposalDao.newVersion(proposal, version);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        version.setId(versionId);
        //Create first version of the proposal and persist to database



        //Create the name of files
        String tomcatRootDirectory = getServletContext().getRealPath("/");

        String ebookFileName = "ebookFile_" + Integer.toString(versionId) + ".pdf";
        String coverImageName = "coverImage_" + Integer.toString(versionId) + ".png";
        version.setEbookFile(new File(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/" + ebookFileName));
        version.setCoverImage(new File(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/" + coverImageName));
        //Create the name of files



        //Update version with correct name of the files
        try {
            proposalDao.updateVersion(version);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //Update version with correct name of the files



        //Create directory for new proposal and create files of the first version
        try {
            FileDAO.createDirectory(Path.of(tomcatRootDirectory + "/../Files/"), Integer.toString(proposalId));

            FileDAO.persistFile(Path.of(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/"), ebookFileName, fileEbookPart.getInputStream());
            FileDAO.persistFile(Path.of(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/"), coverImageName, fileEbookPart.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //Create directory for new proposal and create files of the first version



        //Redirect to confirmation page
        response.sendRedirect("/confirmationPage.jsp?imagePath=bigCheck.png&msg=Your%20publication%20proposal%20has%20been%20successfully%20submitted%2C%20you%20will%20receive%20acknowledgement%20within%2010%20business%20days");
        //Redirect to confirmation page
    }
}
