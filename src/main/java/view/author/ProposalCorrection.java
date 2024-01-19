package view.author;

import com.bookverse.bookverse.sessionConstants.SessionCostants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import proposalManager.Proposal;
import proposalManager.Version;
import proposalManager.WrongStatusException;
import storageSubSystem.FileDAO;
import storageSubSystem.InvalidParameterException;
import storageSubSystem.ProposalDAO;
import userManager.Author;
import userManager.User;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;



@WebServlet(name = "ProposalCorrection", value="/ProposalCorrection")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20, // 20MB
        maxFileSize = 1024 * 1024 * 500,      // 500MB
        maxRequestSize = 1024 * 1024 * 1500)   // 1500MB
public class ProposalCorrection extends HttpServlet {

    protected static String TITLE_PAR = "title";
    protected static String PRICE_PAR = "price";
    protected static String DESCRIPTION_PAR = "description";
    protected static String EBOOKFILE_PAR = "ebookFile";
    protected static String COVERIMAGE_PAR = "coverImage";
    protected static String GENRES_PAR = "genres";
    protected static String PROPOSALID_PAR = "proposalId";
    protected static String NEXT_PAGE = "/publications.jsp";

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

        String proposalId_ = request.getParameter(PROPOSALID_PAR);
        if(proposalId_ == null || proposalId_.isEmpty())
            throw new ServletException("proposalId is not valid");

        int proposalId = Integer.parseInt(proposalId_);
        //Retrieve parameters from request



        //Check if parameters respect the constraints
        if ( ! Proposal.isValidParameter(title, genres, price, description)) {
            throw new ServletException("Parameters are not valid");
        }
        //Check if parameters respect the constraints



        //Retrieve User from the Session
        HttpSession session = (HttpSession) request.getSession();

        User user = (User)session.getAttribute(SessionCostants.USER);
        Author author = user.getRoleAuthor();
        //Retrieve User from the Session



        //Retrieve proposal from the cache
        Set<Proposal> proposals = author.getProposed();
        Proposal proposal = null;
        for(Proposal p : proposals) {
            if(proposalId == p.getId())
                proposal = p;
        }

        if(proposal == null)
            throw new ServletException("This author is not mainAuthor of this proposal");
        //Retrieve proposal from the cache



        //Retrieve data source and build ProposalDAO
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        ProposalDAO proposalDao = new ProposalDAO(ds);
        //Retrieve data source and build ProposalDAO



        //Correct proposal and update proposal's state to database
        try {
            proposal.correct();
        } catch (WrongStatusException e) {
            throw new ServletException("Failed to update state of proposal");
        }

        try {
            proposalDao.updateProposalState(proposal);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to update the state of proposal on database");
        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        }
        //Correct proposal and update proposal's state to database



        //Create a new version and persist to database
        Version lastVersion = null;
        try {
            lastVersion = Version.makeVersion(title, description, price, null, null, null, LocalDate.now(), genres);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }

        try {
            proposal.addVersion(lastVersion);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Failed to add version to proposal");
        }

        int versionId = -1;
        try {
            versionId = proposalDao.newVersion(proposal, lastVersion);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Failed to persist new version on database");
        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        }
        lastVersion.setId(versionId);
        //Create a new version and persist to database



        //Generate file path and add to the version created before
        String tomcatRootDirectory = getServletContext().getRealPath("/");

        String ebookFileName = "ebookFile_" + Integer.toString(versionId) + ".pdf";
        String coverImageName = "coverImage_" + Integer.toString(versionId) + ".png";
        lastVersion.setEbookFile(new File(FileDAO.getFilesDirectory() + Integer.toString(proposalId) + "/" + ebookFileName));
        lastVersion.setCoverImage(new File(FileDAO.getFilesDirectory() + Integer.toString(proposalId) + "/" + coverImageName));
        //Generate file path and add to the version created before



        //Update version to add ebookFileName and coverImageName in database
        try {
            proposalDao.updateVersion(lastVersion);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        }
        //Update version to add ebookFileName and coverImageName in database



        //Persist on filesystem files
        try {
            FileDAO.persistFile(Path.of(FileDAO.getFilesDirectory() + Integer.toString(proposalId)), ebookFileName, fileEbookPart.getInputStream());
            FileDAO.persistFile(Path.of(FileDAO.getFilesDirectory() + Integer.toString(proposalId)), coverImageName, fileCoverImage.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Failed to persist ebookFile or coverImage of the new version");
        }
        //Persist on filesystem files



        //Redirect to the next page
        response.sendRedirect(NEXT_PAGE);
        //Redirect to the next page
    }
}
