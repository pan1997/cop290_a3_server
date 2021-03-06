package cop290.web;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

@WebServlet(urlPatterns = "/complaints/submit",name="Submit")
public class SubmitServlet extends HttpServlet {
    /**
     * Accepts the get request. Parameters are
     * parameter resolved if 1 then resolved complaints only other wise unresolved
     * if no parameters are passed, all complaints are displayed.
     *
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection c = tmpclass.ds.getConnection();
            Statement smt = c.createStatement();
            String title = request.getParameter("title");
            String detail = request.getParameter("detail");
            String level = request.getParameter("level");
            String image=request.getParameter("image");
            String tag_id=request.getParameter("tag_id");
            if(image!=null){
                try{
                    String fnmae=tmpclass.randomName();
                    File f=new File(fnmae);
                    //System.out.println(f.getAbsolutePath());
                    FileOutputStream fout=new FileOutputStream(f);
                    fout.write(image.getBytes());
                    fout.close();
                    //image=fnmae;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            HttpSession session=request.getSession();
            JsonObject user = (JsonObject) session.getAttribute("user");
            response.setContentType("application/json");
            smt.execute("INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES(" + user.getInt("user_id") + ",'"+ title +"','"+ detail+"',"+(image==null?"NULL":"'"+image+"'")+",NOW(),"+"NULL,"+0+","+Integer.parseInt(level)+")");
            ResultSet rs = smt.executeQuery("SELECT LAST_INSERT_ID()");
            String cid=""+rs.getInt(1);
            if(tag_id!=null)
                smt.execute("INSERT INTO Tag_Association(complaint_id, tag_id) VALUES ("+cid+","+tag_id+")");
            rs.next();
            smt.close();
            c.close();
            response.sendRedirect("/complaints/details/"+cid);
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
        System.out.println("POSTED!!!!!!");
        doGet(request, response);
    }
}
