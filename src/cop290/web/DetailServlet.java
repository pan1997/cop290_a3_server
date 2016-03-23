package cop290.web;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pankaj on 23/3/16.
 */
@WebServlet("/complaints/details/*")
public class DetailServlet extends HttpServlet {
    /*
     * Servlet parameters
     * title,tags,detail,lavel,...
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String complaintId;
        {
            StringBuffer req = request.getRequestURL();
            int id = req.indexOf("details/") + 8;
            req = req.delete(0, id);
            id = req.indexOf("/");
            if (id == -1)
                complaintId = req.toString();
            else complaintId = req.substring(0, id);
            if (complaintId.indexOf('?') != -1)
                complaintId = complaintId.substring(0, complaintId.indexOf('?'));
        }
        System.out.println("ComplaintID " + complaintId);
        JsonObject result = Json.createObjectBuilder()
                .add("success", true)
                .build();
        response.setContentType("application/json");
        response.getOutputStream().print(result.toString());
        throw new ServletException("Not implemented");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
