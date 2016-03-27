package cop290.web;

import javax.json.*;
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
import java.util.Enumeration;

/**
 * Created by pankaj on 23/3/16.
 */
@WebServlet(name = "Complaint Details",urlPatterns = "/complaints/details/*")
public class DetailServlet extends HttpServlet {
    /*
     * Servlet parameters
     * title,tags,detail,lavel,...
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        JsonObject user = (JsonObject) session.getAttribute("user");
        response.setContentType("application/json");
        if (user == null)
            response.getOutputStream().print(Json.createObjectBuilder().add("complaints", Json.createArrayBuilder().build()).add("message", "NO LOGIN").build().toString());
        else {
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
            try {
                Connection c = tmpclass.ds.getConnection();
                Statement smt = c.createStatement();
                int upvotes = 0, downvotes = 0;
                ResultSet upvc = smt.executeQuery("SELECT COUNT(*) AS cnt FROM Upvotes WHERE complaint_id=" + complaintId);
                if (upvc.next())
                    upvotes = upvc.getInt("cnt");
                upvc.close();
                upvc = smt.executeQuery("SELECT COUNT(*) AS cnt FROM Downvotes WHERE complaint_id=" + complaintId);
                if (upvc.next())
                    downvotes = upvc.getInt("cnt");
                upvc.close();
                JsonArrayBuilder jbl = Json.createArrayBuilder();
                ResultSet cmnta = smt.executeQuery("SELECT * FROM Comments WHERE complaint_id=" + complaintId);
                while (cmnta.next()) {
                    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                    jsonObjectBuilder.add("user_id", cmnta.getInt("user_id"))
                            .add("detail", cmnta.getString("detail"))
                            .add("date", cmnta.getString("date_commented"));
                    jbl.add(jsonObjectBuilder.build());
                }
                cmnta.close();
                ResultSet rs = smt.executeQuery("SELECT Complaints.*,Users.hostel_id FROM Complaints INNER JOIN Users ON Complaints.user_id = Users.user_id WHERE complaint_id=" + complaintId);
                if (rs.next()) {
                    JsonObject complaint = Json.createObjectBuilder()
                            .add("user_id", rs.getInt("user_id"))
                            .add("title", rs.getString("title"))
                            .add("discritption", rs.getString("discritption"))
                            .add("date_submitted", rs.getString("date_submitted"))
                            .add("level", rs.getInt("level"))
                            .add("resolved", rs.getInt("status") != 0)
                            .add("upvotes", upvotes)
                            .add("downvotes", downvotes)
                            .add("comments", jbl)
                            .add("success", true)
                            .build();
                    if (user.getInt("group_id") == 1 || //admin
                            user.getInt("user_id") == complaint.getInt("user_id") || //submitted by this person
                            complaint.getInt("level") == 1 && user.getInt("hostel_id") == rs.getInt("hostel_id") ||
                            complaint.getInt("level") == 0) {
                        response.getOutputStream().print(complaint.toString());
                        rs.close();
                        Enumeration<String> actions = request.getParameterNames();
                        try {
                            while (actions.hasMoreElements()) {
                                String ac = actions.nextElement();
                                switch (ac) {
                                    case "upvote":
                                        smt.execute("INSERT INTO Upvotes VALUES (" + complaintId + "," + user.getInt("user_id") + ")");
                                        break;
                                    case "downvote":
                                        smt.execute("INSERT INTO Downvotes VALUES (" + complaintId + "," + user.getInt("user_id") + ")");
                                        break;
                                    case "comment":
                                        String cmnt = request.getParameter("comment");
                                        smt.execute("INSERT INTO Comments(user_id, complaint_id, detail, date_commented) VALUES (" + user.getInt("user_id") + "," + complaintId + "," + cmnt + ",NOW())");
                                }
                            }
                        } catch (SQLException s) {
                            s.printStackTrace();
                        }
                    } else
                        response.sendError(403, "Access Denied");
                } else {
                    response.sendError(404, "Complaint contFound");
                }
                rs.close();
                smt.close();
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException(e);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}