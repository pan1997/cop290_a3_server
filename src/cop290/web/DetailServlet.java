package cop290.web;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

/**
 * Created by pankaj on 25/3/16.
 * This Servlet displays the details of the complaint including number of upvotes and downvotes and comments
 * It also aloows the user to upvote or donvote or comment or resolve if he/she has the appropriate permission
 * After the action it reload the page with new information.
 */
@WebServlet(name = "Complaint Details",urlPatterns = "/complaints/details/*")
public class DetailServlet extends HttpServlet {
    /**
     * Servlet takes the action to perform(may be multiple) via GET performs the action and then relodas
     * If there are no actions, no relaoding is done.
     * It displays Comments upvotes and downvotes also.
     * Resolving permisssion is determined as folloows
     * Admin can resolve anything.
     * Indivdual Complaints can be resolved only by submitter
     * Hostel complaints by the warden of the hostel
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
            //System.out.println("ComplaintID " + complaintId);
            JsonArrayBuilder jbl = Json.createArrayBuilder();
            try {
                Connection c = tmpclass.ds.getConnection();
                Statement smt = c.createStatement();
                boolean action = request.getParameterMap().size() > 0;
                int upvotes = 0, downvotes = 0;
                if (!action) {
                    ResultSet upvc = smt.executeQuery("SELECT COUNT(*) AS cnt FROM Upvotes WHERE complaint_id=" + complaintId);
                    if (upvc.next())
                        upvotes = upvc.getInt("cnt");
                    upvc.close();
                    upvc = smt.executeQuery("SELECT COUNT(*) AS cnt FROM Downvotes WHERE complaint_id=" + complaintId);
                    if (upvc.next())
                        downvotes = upvc.getInt("cnt");
                    upvc.close();
                    ResultSet cmnta = smt.executeQuery("SELECT Comments.*,Users.first_name,Users.last_name FROM Comments INNER JOIN Users ON Comments.user_id = Users.user_id WHERE complaint_id=" + complaintId);
                    while (cmnta.next()) {
                        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                        jsonObjectBuilder.add("user_id", cmnta.getInt("user_id"))
                                .add("name", cmnta.getString("first_name") + " " + cmnta.getString("last_name"))
                                .add("detail", cmnta.getString("detail"))
                                .add("date", cmnta.getString("date_commented"));
                        jbl.add(jsonObjectBuilder.build());
                    }
                    cmnta.close();
                }
                ResultSet rs = smt.executeQuery("SELECT Complaints.*,Users.hostel_id,Users.first_name,Users.last_name FROM Complaints INNER JOIN Users ON Complaints.user_id = Users.user_id WHERE complaint_id=" + complaintId);
                if (rs.next()) {
                    String imageLoc = rs.getString("image");
                    String img = null;
                    try {
                        if (imageLoc != null && !imageLoc.equals("NULL")) {
                            FileInputStream file = new FileInputStream(imageLoc);
                            byte[] tmp = new byte[file.available()];
                            int n = file.read(tmp);
                            img = new String(tmp);
                        }
                    } catch (Exception e) {
                        System.err.println("Image read failed");
                        e.printStackTrace();
                    }
                    JsonObject complaint = Json.createObjectBuilder()
                            .add("user_id", rs.getInt("user_id"))
                            .add("name", rs.getString("first_name") + " " + rs.getString("last_name"))
                            .add("title", rs.getString("title"))
                            .add("discritption", rs.getString("discritption"))
                            .add("date_submitted", rs.getString("date_submitted"))
                            .add("level", rs.getInt("level"))
                            .add("resolved", rs.getInt("status") != 0)
                            .add("image", img == null ? "NULL" : img)
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
                                        //ResultSet rs1 = smt.executeQuery("SELECT * FROM Downvotes WHERE complaint_id=" + Integer.toString(user.getInt("user_id")) + ")");
                                        //if(!(rs1.next()))
                                        smt.execute("INSERT INTO Upvotes VALUES (" + complaintId + "," + user.getInt("user_id") + ")");
                                        break;
                                    case "downvote":
                                        smt.execute("INSERT INTO Downvotes VALUES (" + complaintId + "," + user.getInt("user_id") + ")");
                                        break;
                                    case "resolve":
                                        boolean permission = false;
                                        if (user.getInt("group_id") == 1)
                                            permission = true;
                                        else if (complaint.getInt("level") == 2 && complaint.getInt("user_id") == user.getInt("user_id"))
                                            permission = true;
                                        else if (complaint.getInt("level") == 1) {
                                            ResultSet rst = smt.executeQuery("SELECT * FROM Hostel_Wardens " +
                                                    "INNER JOIN Users ON Hostel_Wardens.hostel_id=Users.hostel_id " +
                                                    "INNER JOIN Complaints ON Complaints.user_id=Users.user_id " +
                                                    "WHERE complaint_id=" + complaintId + " AND Hostel_Wardens.user_id=" + user.getInt("user_id"));
                                            if (rs.next())
                                                permission = true;
                                        } else if (complaint.getInt("level") == 0 && user.getInt("group_id") == 2)
                                            permission = true;
                                        if (permission)
                                            smt.execute("UPDATE Complaints SET status=1 WHERE complaint_id=" + complaintId);
                                        else response.sendError(403);
                                        break;
                                    case "comment":
                                        String cmnt = request.getParameter("comment");
                                        smt.execute("INSERT INTO Comments(user_id, complaint_id, detail, date_commented) VALUES (" + user.getInt("user_id") + "," + complaintId + ",'" + cmnt + "',NOW())");
                                }
                            }
                            if(session.getAttribute("web")!=null){
                                if (action) {
                                    smt.close();
                                    c.close();
                                    response.sendRedirect("/complaints/details/" + complaintId);
                                }
                            }else {
                                if (action) {
                                    smt.close();
                                    c.close();
                                    response.sendRedirect("/complaints/details/" + complaintId);
                                }
                            }

                        } catch (SQLException s) {
                            s.printStackTrace();
                        }
                    } else {
                        smt.close();
                        c.close();
                        response.sendError(403);
                    }
                } else {
                    smt.close();
                    c.close();
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

    /**
     * Servlet takes the action to perform(may be multiple) via POST performs the action and then relodas
     * If there are no actions, no relaoding is done.
     * It displays Comments upvotes and downvotes also.
     * Resolving permisssion is determined as folloows
     * Admin can resolve anything.
     * Indivdual Complaints can be resolved only by submitter
     * Hostel complaints by the warden of the hostel
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}