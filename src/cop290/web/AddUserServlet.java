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
 * This Servlet allows the administrator to create new Users.
 * All the parametesrs can be set here for the new user.
 */
@WebServlet(name = "AddUserServlet",urlPatterns = "/admin/addUser")
public class AddUserServlet extends HttpServlet {

    /**
     * Accepts the post requests. Just passes the requests to doGet
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Accepts the GET requests. The accepted GET parameters are
     * parameter first_name
     * parameter last_name
     * parameter login
     * parameter password
     * parameter group_id
     * parameter department_id
     * parameter entry_number
     * parameter hostel_id
     * returns a JSONObject with a success parameter.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        JsonObject user = (JsonObject) session.getAttribute("user");
        if (user == null || user.getInt("group_id") != 1)
            response.getOutputStream().print(Json.createObjectBuilder().add("SUCCESS", false).add("MESSAGE", user == null ? "NO LOGIN" : "NO PRIVELAGE").build().toString());
        else {
            String first_name = request.getParameter("first_name");
            String last_name = request.getParameter("last_name");
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String group_id = request.getParameter("group_id");
            String department_id = request.getParameter("department_id");
            String entry_number = request.getParameter("entry_number");
            String hostel_id = request.getParameter("hostel_id");
            try {
                Connection conn = tmpclass.ds.getConnection();
                Statement smt = conn.createStatement();
                boolean success = true;
                try {
                    smt.execute("INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES " +
                            "(" + group_id + ",'" + first_name + "','" + last_name + "','" + login + "','" + password + "'," + department_id + "," + hostel_id + ",'" + (entry_number == null ? "NULL" : entry_number) + "')");
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
