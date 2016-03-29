package cop290.web;

import javax.json.Json;
import javax.json.JsonArray;
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
 * Created by kritarth on 28/3/16.
 */
@WebServlet(urlPatterns = "/search",name="Search")

//This Servlet add the functionality of searching keywords in the Title of the submitted complaints
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //query string is fetched from the request
            String query = request.getParameter("query");
            //session is obtained
            HttpSession session = request.getSession();
            //user JsonObject is created and obtained from the session
            JsonObject user = (JsonObject) session.getAttribute("user");
            //content type set for response
            response.setContentType("application/json");
            //if no user is found logged into session then NO LOGIN response is sent to the client
            if (user == null)
                response.getOutputStream().print(Json.createObjectBuilder().add("Search Complaints", Json.createArrayBuilder().build()).add("message", "NO LOGIN").build().toString());
                //else a connection is initiated with the sql database and the query is searched for in the Complaints
            else {
                Connection c = tmpclass.ds.getConnection();
                Statement smt = c.createStatement();
                JsonArrayBuilder jb = Json.createArrayBuilder();
                ResultSet rs = smt.executeQuery("SELECT Complaints.*,Users.hostel_id,Users.first_name,Users.last_name FROM Complaints INNER JOIN Users ON Complaints.user_id = Users.user_id WHERE Complaints.title LIKE '%" + query + "%';");
                //JSONObject is created for all the results found
                while (rs.next())
                    jb.add(tmpclass.getComplaintSummary(rs));
                JsonArray lst = jb.build();
                response.getOutputStream().print(lst.toString());
                smt.close();
                rs.close();
                c.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            throw e;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}