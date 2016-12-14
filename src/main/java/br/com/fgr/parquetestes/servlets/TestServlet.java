package br.com.fgr.parquetestes.servlets;

import br.com.fgr.parquetestes.domain.OnCompletedOperation;
import br.com.fgr.parquetestes.domain.TestExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(asyncSupported = true, urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private List<AsyncContext> asyncContext;

    public TestServlet() {
        super();
        asyncContext = new ArrayList<>();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(10 * 60 * 1000);
        addToWaitingList(asyncContext);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uploadDirectory = request.getParameter("dir");
        String alias = request.getParameter("alias");
        String password = request.getParameter("password");
        String uploadPath = getServletContext().getRealPath("") + File.separator + uploadDirectory;

        new Thread(() -> {
            if (!uploadDirectory.isEmpty()) {
                TestExecutor executor = new TestExecutor(uploadDirectory, new OnCompletedOperation() {

                    @Override
                    public void onSuccess(String message) {
                        sendMessage(message);
                    }

                    @Override
                    public void onError(String messageError) {
                        sendMessage(messageError);
                    }
                });
                executor.createScripts(uploadPath, "app.keystore", alias, password);
                executor.genSkeleton();
                executor.changeFilesLocation();
                executor.resignApk();
                executor.runTests(uploadPath);
            }
        }).start();
    }

    private void addToWaitingList(AsyncContext c) {
        asyncContext.add(c);
    }

    private void sendMessage(String message) {
        asyncContext.forEach((AsyncContext ac) -> {
            try (PrintWriter writer = ac.getResponse().getWriter()) {
                writer.println(message);
                writer.flush();
                ac.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
