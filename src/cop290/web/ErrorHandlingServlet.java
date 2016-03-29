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
 * This is the error servlet that is displayed whenever some error occurs
 * It displays the stach trace and error name
 */
@WebServlet(urlPatterns = "/error",name="Error Handling Servlet")
public class ErrorHandlingServlet extends HttpServlet {
    /**
     * Displays the error name and stack trace
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Inside error");
        response.setContentType("text/html");
        ServletOutputStream sop = response.getOutputStream();
        sop.print("<!DOCTYPE html>\n<html><head></head><body>");
        Throwable th = (Throwable) request.getAttribute("javax.servlet.error.exception");
        sop.print(th == null ? "NULL" : th.toString());
        if (th != null)
            th.printStackTrace(response.getWriter());
        sop.print("<br><a href=\"index.jsp\">Home</a>");
        sop.print("</body></html>");
    }

    /**
     * Displays the error name and stack trace
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}