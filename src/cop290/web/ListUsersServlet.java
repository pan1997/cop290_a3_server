package cop290.web;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by pankaj on 29/3/16.
 * Returns the list of all users
 * Accecible only to admin
 */
@WebServlet(name = "ListUsersServlet",urlPatterns = "/users")
public class ListUsersServlet extends HttpServlet {
    /**
     * Lists the name of all users to the admin
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    /**
     * Lists the name of all users to the admin
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        JsonObject user = (JsonObject) session.getAttribute("user");
        if (user == null || user.getInt("group_id") != 1)
            response.getOutputStream().print(Json.createObjectBuilder().add("SUCCESS", false).add("MESSAGE", user == null ? "NO LOGIN" : "NO PRIVELAGE").build().toString());
        else {

            try {
                Connection conn = tmpclass.ds.getConnection();
                Statement smt = conn.createStatement();
                ResultSet rs=smt.executeQuery("SELECT * FROM Users");
                JsonArrayBuilder jbl=Json.createArrayBuilder();
                while(rs.next())
                    jbl.add(Json.createObjectBuilder().add("login",rs.getString("login"))
                            .add("name",rs.getString("first_name")+" "+rs.getString("last_name"))
                            .add("user_id",rs.getInt("user_id"))
                            .build());
                JsonObject result=Json.createObjectBuilder()
                        .add("users",jbl)
                        .add("success",true)
                        .build();
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
