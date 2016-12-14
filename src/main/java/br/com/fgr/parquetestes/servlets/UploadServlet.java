package br.com.fgr.parquetestes.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static String UPLOAD_DIRECTORY = "";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    public UploadServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        UPLOAD_DIRECTORY = String.valueOf(System.currentTimeMillis());
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter printWriter = response.getWriter();
            printWriter.println("Não contém dados.");
            printWriter.flush();
            return;
        }

        String password = "";
        String alias = "";

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            List<FileItem> formItems = upload.parseRequest(request);
            for (FileItem item : formItems) {
                if (!item.isFormField()) {
                    if (item.getName().isEmpty()) {
                        continue;
                    }
                    String fileName = new File(item.getFieldName()).getName();
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    // saves the file on disk
                    item.write(storeFile);
                } else {
                    if (item.getFieldName().equals("pwd")) {
                        password = item.getString();
                    }
                    if (item.getFieldName().equals("alias")) {
                        alias = item.getString();
                    }
                }
            }
            request.setAttribute("message", "Upload feito com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Deu ruim.");
        }

        request.setAttribute("dir", UPLOAD_DIRECTORY);
        request.setAttribute("alias", alias);
        request.setAttribute("password", password);

		/*
         * Executor executor = new Executor(UPLOAD_DIRECTORY, new
		 * OnCompletedOperation() {
		 *
		 * @Override public void onSuccess() {
		 *
		 * }
		 *
		 * @Override public void onError(String messageError) {
		 *
		 * } }); executor.createScripts(uploadPath, "app.keystore", alias,
		 * password); executor.genSkeleton(); executor.changeFilesLocation();
		 * executor.resignApk(); executor.runTests(uploadPath);
		 */

        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
    }
}
