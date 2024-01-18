package view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import storageSubSystem.FileDAO;

import java.io.*;

@WebServlet(name = "FileDownload", value = "/FileDownload")
public class FileDownload extends HttpServlet {

    protected static String FILENAME_PAR = "fileName"; //intended like  <p.id>/ebookfile_<v.id>

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String fileToDownload = request.getParameter(FILENAME_PAR);

        if(fileToDownload == null || fileToDownload.isEmpty()) {
            throw new ServletException("filename value not valid");
        }

        fileToDownload = FileDAO.getFilesDirectory() + fileToDownload;
        File file = new File(fileToDownload);

        if(! file.exists()) {
            throw new ServletException("File not found");
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

        try (FileInputStream fis = new FileInputStream(file)) {

            OutputStream os = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("File not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("IOException");
        }
    }
}