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
@WebServlet("/complaints/submit")
public class SubmitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = Json.createObjectBuilder()
                .add("success", true)
                .build();
        response.setContentType("application/json");
        response.getOutputStream().print(result.toString());
        throw new ServletException("Not implemented");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
