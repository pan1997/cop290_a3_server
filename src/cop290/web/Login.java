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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet(urlPatterns = "/login",name="Login")
public class Login extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println("Attempting Login via "+name+" "+password);
            HttpSession session=request.getSession();
            JsonObject user=tmpclass.getUserJson(name,password);
            if(user!=null)
                session.setAttribute("user",user);
            JsonObject result = Json.createObjectBuilder()
                    .add("success", user!=null)
                    .add("user",user==null?Json.createObjectBuilder().build():user)
                    .build();
            if(session.getAttribute("web")!=null){
                response.sendRedirect("index.jsp");
            }else {
                response.setContentType("application/json");
                response.getOutputStream().print(result.toString());
            }
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