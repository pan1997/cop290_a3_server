package cop290.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
    public Login() {
        super();
    }
    public void init() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("username");
            String password = request.getParameter("password");
            JsonObject user=tmpclass.getUserJson(name,password);
            JsonObject result = Json.createObjectBuilder()
                    .add("success", user!=null)
                    .add("user",user==null?Json.createObjectBuilder().build():user)
                    .build();
            response.setContentType("application/json");
            response.getOutputStream().print(result.toString());
        }
        catch (SQLException e){
            e.printStackTrace();
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