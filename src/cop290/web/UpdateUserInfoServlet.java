package cop290.web;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by pankaj on 29/3/16.
 * Allows the user to change his user info including password
 */
@WebServlet(name = "UpdateUserInfoServlet",urlPatterns = "/user/update")
public class UpdateUserInfoServlet extends HttpServlet {
    /**
     * Accepts the get request parameters are the new values
     * return success of the operation
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    /**
     * Accepts the get request parameters are the new values
     * return success of the operation
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session=request.getSession();
        JsonObject user=(JsonObject)session.getAttribute("user");
        if(user==null)
            response.getOutputStream().print(Json.createObjectBuilder().add("MESSAGE","NO LOGIN").add("SUCCESS",false).build().toString());
        else{
            try{
                Connection conn=tmpclass.ds.getConnection();
                Statement smt=conn.createStatement();
                //String listOfParams="";
                if(request.getParameter("password")!=null) {
                    boolean success=true;
                    try{
                        smt.execute("UPDATE Users SET password='" + request.getParameter("password") + "' WHERE user_id=" + user.getInt("user_id"));
                    }catch (Exception e){
                        e.printStackTrace();
                        success=false;
                    }
                    response.getOutputStream().print(Json.createObjectBuilder().add("SUCCESS",success).build().toString());
                }
                else response.getOutputStream().print(Json.createObjectBuilder().add("SUCCESS",true).build().toString());
                //smt.execute("UPDATE Users SET "+" WHERE user_id="+user.getInt("user_id"));
                smt.close();
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
                throw new ServletException(e);
            }

        }
    }
}
