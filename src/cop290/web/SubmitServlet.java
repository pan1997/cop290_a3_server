package cop290.web;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by pankaj on 23/3/16.
 */
@WebServlet(urlPatterns = "/complaints/submit",name="Submit")
public class SubmitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session=request.getSession();
            JsonObject user=(JsonObject)session.getAttribute("user");

            JsonObject result=null;

            if(session.getAttribute("web")!=null){
                response.sendRedirect("index.jsp");
            }else {
                response.setContentType("application/json");
                response.getOutputStream().println(""+response);
            }
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            throw e;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
