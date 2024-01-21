import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import proposalManager.Proposal;
import proposalManager.Version;
import storageSubSystem.AuthorDAO;
import storageSubSystem.InvalidParameterException;
import storageSubSystem.ProposalDAO;
import storageSubSystem.ValidatorDAO;
import userManager.Author;
import userManager.User;
import userManager.Validator;
import view.author.ProposalCreation;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

public class testServlet {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private User user;
    @Mock
    private ProposalDAO proposalDAO;
    @Mock
    private ValidatorDAO validatorDAO;
    @Mock
    private AuthorDAO authorDAO;

    @Test
    public void test() throws Exception {

        proposalDAO = Mockito.mock(ProposalDAO.class);
        Mockito.lenient().when(proposalDAO.newProposal(any(Proposal.class))).thenReturn(1);
        Mockito.lenient().when(proposalDAO.newVersion(any(Proposal.class), any(Version.class))).thenReturn(1);
        Mockito.doNothing().when(proposalDAO).updateVersion(any(Version.class));



        validatorDAO = Mockito.mock(ValidatorDAO.class);
        Mockito.lenient().when(validatorDAO.findFreeValidator(any(Author.class), any(Set.class))).thenReturn(new Validator());



        authorDAO = Mockito.mock(AuthorDAO.class);
        Mockito.lenient().when(authorDAO.findByID(1)).thenReturn(new Author());


        //Mock the request
        request = Mockito.mock(HttpServletRequest.class);
        Mockito.lenient().when(request.getParameter("title")).thenReturn("title");
        Mockito.lenient().when(request.getParameter("price")).thenReturn("10");
        Mockito.lenient().when(request.getParameter("description")).thenReturn("description");

        Part fileEbook = Mockito.mock(Part.class);

        Mockito.lenient().when(fileEbook.getSize()).thenReturn(1024L); // Return size in bytes
        Mockito.lenient().when(fileEbook.getInputStream()).thenReturn(Mockito.mock(InputStream.class));

        Mockito.lenient().when(request.getPart("ebookFile")).thenReturn(fileEbook);

        Part coverImage = Mockito.mock(Part.class);

        Mockito.lenient().when(fileEbook.getSize()).thenReturn(1024L); // Return size in bytes
        Mockito.lenient().when(fileEbook.getInputStream()).thenReturn(Mockito.mock(InputStream.class));

        Mockito.lenient().when(request.getPart("coverImage")).thenReturn(coverImage);

        String[] genres = new String[2];
        genres[0] = "genere1";
        genres[1] = "genere2";
        Mockito.lenient().when(request.getParameterValues("genres")).thenReturn(genres);
        //Mock the request



        //Mock of a session
        session = Mockito.mock(HttpSession.class);
        Mockito.lenient().when(request.getSession()).thenReturn(session);

        user = Mockito.mock(User.class);
        Mockito.lenient().when(session.getAttribute("user")).thenReturn(user);

        Mockito.lenient().when(user.getRoleAuthor()).thenReturn(new Author());
        //Mock of a session



        //Mock others
        ServletContext context = Mockito.mock(ServletContext.class);
        Mockito.lenient().when(request.getServletContext()).thenReturn(context);
        Mockito.lenient().when(request.getServletContext().getRealPath("/")).thenReturn("C:\\Users\\Tonaion\\Desktop\\Munezzz");
        //Mock others


        response = Mockito.mock(HttpServletResponse.class);

        ProposalCreation proposalCreation = new ProposalCreation(proposalDAO, authorDAO, validatorDAO);

        proposalCreation.doPost(request, response);


        Mockito.verify(response).sendRedirect("/confirmationPage.jsp?imagePath=bigCheck.png&msg=Your%20publication%20proposal%20has%20been%20successfully%20submitted%2C%20you%20will%20receive%20acknowledgement%20within%2010%20business%20days");
    }
}