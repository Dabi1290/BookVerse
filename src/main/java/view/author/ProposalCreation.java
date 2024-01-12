package view.author;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import proposalManager.Proposal;
import proposalManager.Version;
import storageSubSystem.AuthorDAO;
import storageSubSystem.BaseFileDAO;
import storageSubSystem.ProposalDAO;
import storageSubSystem.ValidatorDAO;
import userManager.Author;
import userManager.User;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;


@WebServlet(name = "ProposalCreation", value="/ProposalCreation")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20, // 20MB
        maxFileSize = 1024 * 1024 * 100,      // 100MB
        maxRequestSize = 1024 * 1024 * 500)   // 500MB
public class ProposalCreation extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String title = request.getParameter("title");
        if(title == null || title.isEmpty())
            throw new ServletException("title not valid");

        String price_ = request.getParameter("price");
        if(price_ == null || price_.isEmpty())
            throw new ServletException("price not valid");
        int price = Integer.parseInt(price_);
        if(price < 0)
            throw new ServletException("price not valid value");

        String description = request.getParameter("description");
        if(description == null || description.isEmpty())
            throw new ServletException("description not valid");

        Part fileEbookPart = request.getPart("ebookFile");
        if(fileEbookPart == null)
            throw new ServletException("ebookFile not valid");

        Part fileCoverImage = request.getPart("coverImage");
        if(fileCoverImage == null)
            throw new ServletException("coverImage not valid");

        String[] genresParameter = request.getParameterValues("genres");
        if(genresParameter == null || genresParameter.length == 0)
            throw new ServletException("genres not valid");

        Set<String> genres = new TreeSet<>(Arrays.asList(genresParameter));

        String[] authors = request.getParameterValues("authors");
        if(authors == null || authors.length == 0)
            throw new ServletException("auhtors not valid");



        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");



        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        Author mainAuthor = user.getRoleAuthor();

        AuthorDAO authorDao = new AuthorDAO(ds);
        Set<Author> coAuthors = new TreeSet<>();
        for(String authorId_ : authors) {
            try {
                Author coAuthor = authorDao.findByID(Integer.parseInt(authorId_));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }



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



        Version version = Version.makeVersion(title, description, price, null, null, null, LocalDate.now(),  genres);
        proposal.addVersion(version);

        int versionId = -1;
        try {
            versionId = proposalDao.persistVersion(proposal, version);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        version.setId(versionId);



        String tomcatRootDirectory = getServletContext().getRealPath("/");

        String ebookFileName = "ebookFile_" + Integer.toString(versionId) + ".pdf";
        String coverImageName = "coverImage_" + Integer.toString(versionId) + ".png";
        version.setEbookFile(new File(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/" + ebookFileName));
        version.setCoverImage(new File(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/" + coverImageName));

        try {
            proposalDao.updateVersion(version);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        try {
            BaseFileDAO.createDirectory(Path.of(tomcatRootDirectory + "/../Files/"), Integer.toString(proposalId));

            BaseFileDAO.persistFile(Path.of(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/"), ebookFileName, fileEbookPart.getInputStream());
            BaseFileDAO.persistFile(Path.of(tomcatRootDirectory + "/../Files/" + Integer.toString(proposalId) + "/"), coverImageName, fileEbookPart.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
