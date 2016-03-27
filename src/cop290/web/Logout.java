package cop290.web;

import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logout
 */
@WebServlet(name="Logout",urlPatterns = "/logout")
public class Logout extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject result = Json.createObjectBuilder()
                .add("success", true)
                .build();
        boolean web = request.getSession().getAttribute("web") != null;
        request.getSession().invalidate();
        if (web)
            response.sendRedirect("index.jsp");
        else {
            response.setContentType("application/json");
            response.getOutputStream().print(result.toString());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}