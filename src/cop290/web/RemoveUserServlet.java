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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by pankaj on 28/3/16.
 * This Servlet allows the administrator to remove Users.
 */
@WebServlet(name = "RemoveUserServlet",urlPatterns = "/admin/removeUser")
public class RemoveUserServlet extends HttpServlet {
    /**
     * accepts the post request. parameters are
     * parameter login
     * return success of the attempt
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * accepts the post request. parameters are
     * parameter login
     * return success of the attempt
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        JsonObject user = (JsonObject) session.getAttribute("user");
        if (user == null || user.getInt("group_id") != 1)
            response.getOutputStream().print(Json.createObjectBuilder().add("SUCCESS", false).add("MESSAGE", user == null ? "NO LOGIN" : "NO PRIVELAGE").build().toString());
        else {
            String login = request.getParameter("login");
            try {
                Connection conn = tmpclass.ds.getConnection();
                Statement smt = conn.createStatement();
                boolean success = true;
                try {
                    smt.execute("DELETE FROM Users WHERE login='" + login + "'");
                } catch (Exception e) {
                    success = false;
                    e.printStackTrace();
                }
                JsonObject result = Json.createObjectBuilder().add("SUCCESS", success).build();
                smt.close();
                conn.close();
                response.getOutputStream().print(result.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException(e);
            }

        }
    }
}