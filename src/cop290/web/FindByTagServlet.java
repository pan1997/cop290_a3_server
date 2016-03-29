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
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by pankaj on 29/3/16.
 * This servlet searches and returns the lsit of complaints for a given tag
 */
@WebServlet(name = "FindByTagServlet",urlPatterns = "/complaints/find_by_tag")
public class FindByTagServlet extends HttpServlet {
    /**
     * Accepts the GET Request. parameters are
     * parameter tag_id
     * returs the json containg the summaries of the compolaints.
     * Note only the complaints that are visible are shown.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    /**
     * Accepts the GET Request. parameters are
     * parameter tag_id
     * returs the json containg the summaries of the compolaints.
     * Note only the complaints that are visible are shown.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        JsonObject user = (JsonObject) session.getAttribute("user");
        if (user == null) {
            response.getOutputStream().println(Json.createObjectBuilder().add("SUCCESS", false).add("MESSAGE", "NO LOGIN").build().toString());
        } else {
            try {
                System.out.println("TagId " + request.getParameter("tag_id"));
                Connection conn = tmpclass.ds.getConnection();
                Statement smt = conn.createStatement();
                String tag_id = request.getParameter("tag_id");
                ResultSet rs;
                if (user.getInt("group_id") == 1)
                    rs = smt.executeQuery("SELECT Complaints.*,Users.first_name,Users.last_name FROM Complaints INNER JOIN Tag_Association ON Complaints.complaint_id = Tag_Association.complaint_id INNER JOIN Users ON Users.user_id=Complaints.user_id WHERE tag_id=" + tag_id);
                else {
                    System.out.println("SELECT Complaints.*,Users.first_name,Users.last_name FROM Complaints INNER JOIN Tag_Association ON Complaints.complaint_id = Tag_Association.complaint_id INNER JOIN Users ON Users.user_id=Complaints.user_id WHERE tag_id=" + tag_id + " AND IF(Complaints.level=0,1,IF(Complaints.level=1 AND Users.hostel_id=" + user.getInt("hostel_id") + ",1,IF(Complaints.level=2 AND Complaints.user_id=" + user.getInt("user_id") + ")))=1");
                    rs = smt.executeQuery("SELECT Complaints.*,Users.first_name,Users.last_name FROM Complaints INNER JOIN Tag_Association ON Complaints.complaint_id = Tag_Association.complaint_id INNER JOIN Users ON Users.user_id=Complaints.user_id WHERE tag_id=" + tag_id + " AND IF(Complaints.level=0,1,IF(Complaints.level=1 AND Users.hostel_id=" + user.getInt("hostel_id") + ",1,IF(Complaints.level=2 AND Complaints.user_id=" + user.getInt("user_id") + ",1,0)))=1");
                }
                JsonArrayBuilder jb = Json.createArrayBuilder();
                while (rs.next())
                    jb.add(tmpclass.getComplaintSummary(rs));
                JsonArray lst = jb.build();
                rs.close();
                rs = smt.executeQuery("SELECT * FROM Tags WHERE tag_id=" + tag_id);
                rs.next();
                JsonObject result = Json.createObjectBuilder()
                        .add("tag", rs.getString("tag_name"))
                        .add("complaints", lst)
                        .add("message", "success")
                        .build();
                smt.close();
                conn.close();
                response.getOutputStream().print(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
