package cop290.web;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pankaj on 19/3/16.
 */
@WebServlet(urlPatterns = "/error",name="Error Handling Servlet")
public class ErrorHandlingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Inside error");
        response.setContentType("text/html");
        ServletOutputStream sop = response.getOutputStream();
        sop.print("<!DOCTYPE html>\n<html><head></head><body>");
        Throwable th = (Throwable) request.getAttribute("javax.servlet.error.exception");
        sop.print(th == null ? "NULL" : th.toString());
        sop.print("<br><a href=\"index.jsp\">Home</a>");
        sop.print("</body></html>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
