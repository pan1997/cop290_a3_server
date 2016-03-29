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
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String query = request.getParameter("query");
            //String[] fst = query.split("\\s+");
            HttpSession session = request.getSession();
            JsonObject user = (JsonObject) session.getAttribute("user");
            response.setContentType("application/json");
            if (user == null)
                response.getOutputStream().print(Json.createObjectBuilder().add("Search Complaints", Json.createArrayBuilder().build()).add("message", "NO LOGIN").build().toString());
            else {
                Connection c = tmpclass.ds.getConnection();
                Statement smt = c.createStatement();
                JsonArrayBuilder jb = Json.createArrayBuilder();
                //ResultSet rs = smt.executeQuery(";");
                    /*
                    for(int i=0; i<fst.length; i++){
                        System.out.println(fst[i]);
                        rs = smt.executeQuery("SELECT * FROM Complaints WHERE Complaints.title LIKE '%" + fst[i] +"%';");
                        while (rs.next())
                            jb.add(tmpclass.getComplaintSummary(rs));
                    }
                    */
                //ResultSet rs = smt.executeQuery("SELECT * FROM Complaints WHERE Complaints.title LIKE '%" + query +"%';");
                ResultSet rs = smt.executeQuery("SELECT Complaints.*,Users.hostel_id,Users.first_name,Users.last_name FROM Complaints INNER JOIN Users ON Complaints.user_id = Users.user_id WHERE Complaints.title LIKE '%" + query + "%';");

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