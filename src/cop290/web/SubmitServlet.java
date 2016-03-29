package cop290.web;

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
import java.sql.ResultSet;

@WebServlet(urlPatterns = "/complaints/submit",name="Submit")
public class SubmitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection c = tmpclass.ds.getConnection();
            Statement smt = c.createStatement();
            String title = request.getParameter("title");
            String detail = request.getParameter("detail");
            String level = request.getParameter("level");
            String image=request.getParameter("image");
            if(image!=null){
                try{
                    String fnmae=tmpclass.randomName();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            HttpSession session=request.getSession();
            JsonObject user = (JsonObject) session.getAttribute("user");
            response.setContentType("application/json");
            smt.execute("INSERT INTO Complaints(user_id, title, discritption, date_submitted, date_resolved, status, level) VALUES(" + user.getInt("user_id") + ",'"+ title +"','"+ detail+"',"+"NOW(),"+"NULL,"+0+","+Integer.parseInt(level)+")");
            ResultSet rs = smt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            response.sendRedirect("/complaints/details/"+rs.getInt(1));
            smt.close();
            c.close();
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new ServletException(e);
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            throw e;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
