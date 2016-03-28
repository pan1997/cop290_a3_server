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
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by pankaj on 28/3/16.
 */
@WebServlet(name = "AddUserServlet",urlPatterns = "/admin/addUser")
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        JsonObject user=(JsonObject) session.getAttribute("user");
        if(user==null||user.getInt("group_id")!=1)
            response.getOutputStream().print(Json.createObjectBuilder().add("SUCCESS",false).add("MESSAGE",user==null?"NO LOGIN":"NO PRIVELAGE").build().toString());
        else{
            String first_name=request.getParameter("first_name");
            String last_name=request.getParameter("last_name");
            String login=request.getParameter("login");
            String password=request.getParameter("password");
            String group_id=request.getParameter("group_id");
            String department_id=request.getParameter("department_id");
            String entry_number=request.getParameter("entry_number");
            String hostel_id=request.getParameter("hostel_id");

            try{
                Connection conn=tmpclass.ds.getConnection();
                Statement smt=conn.createStatement();
                String cmd="INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES " +
                        "("+group_id+",'"+first_name+"','"+last_name+"','"+login+"','"+password+"',"+department_id+","+hostel_id+",'"+entry_number+"')";
                System.out.println(cmd);
                smt.execute("INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES " +
                        "("+group_id+",'"+first_name+"','"+last_name+"','"+login+"','"+password+"',"+department_id+","+hostel_id+",'"+entry_number+"')");
            }catch (SQLException e){
                e.printStackTrace();
                throw new ServletException(e);
            }

        }
    }
}
