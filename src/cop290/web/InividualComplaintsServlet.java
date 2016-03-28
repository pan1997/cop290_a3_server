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
import java.sql.Statement;

/**
 * Created by pankaj on 23/3/16.
 */
@WebServlet(urlPatterns = "/complaints/individual",name="Individual Level Complaints")
public class InividualComplaintsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        JsonObject user = (JsonObject) session.getAttribute("user");
        if (user == null)
            response.getOutputStream().print(Json.createObjectBuilder().add("complaints", Json.createArrayBuilder().build()).add("message", "NO LOGIN").build().toString());
        else
            try {
                Connection conn = tmpclass.ds.getConnection();
                Statement stmt = conn.createStatement();
                String st=request.getParameter("resolved");
                ResultSet rs = stmt.executeQuery("SELECT * FROM Complaints WHERE user_id='" + user.getInt("user_id") + "' AND level=2"+(st!=null?" AND Complaints.status="+st:""));
                JsonArrayBuilder jb = Json.createArrayBuilder();
                while (rs.next())
                    jb.add(tmpclass.getComplaintSummary(rs));
                JsonArray lst = jb.build();
                stmt.close();
                conn.close();
                JsonObject result = Json.createObjectBuilder()
                        .add("complaints", lst)
                        .add("message", "SUCCESS")
                        .build();
                response.getOutputStream().print(result.toString());
            } catch (Exception e) {
                throw new ServletException(e);
            }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}