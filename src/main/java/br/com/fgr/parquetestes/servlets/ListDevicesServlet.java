package br.com.fgr.parquetestes.servlets;

import br.com.fgr.parquetestes.domain.Device;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ListDevicesServlet")
public class ListDevicesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ListDevicesServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(Device.getDevices()));
    }
}
