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
 * Shows the list of all tags
 */
@WebServlet(name = "TagListServlet",urlPatterns = "/tags")
public class TagListServlet extends HttpServlet {
    /*
     * returns the list of all tags along with tagid
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /*
     * returns the list of all tags along with tagid
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            Connection conn = tmpclass.ds.getConnection();
            Statement smt = conn.createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM Tags");
            JsonArrayBuilder jbl = Json.createArrayBuilder();
            while (rs.next())
                jbl.add(Json.createObjectBuilder()
                        .add("tag_id", rs.getInt("tag_id"))
                        .add("tag_name", rs.getString("tag_name"))
                        .build());
            JsonObject result = Json.createObjectBuilder()
                    .add("tags", jbl)
                    .add("success", true)
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
