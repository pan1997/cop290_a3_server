package cop290.web;

import javax.json.Json;
import javax.json.JsonArray;
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
@WebServlet("/complaints/hostel")
public class HostelComplaintsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonArray lst=Json.createArrayBuilder().build();
        JsonObject result = Json.createObjectBuilder()
                .add("complaints", lst)
                .build();
        response.setContentType("application/json");
        response.getOutputStream().print(result.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
