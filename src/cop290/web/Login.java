package cop290.web;

import java.io.IOException;

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

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

    public void init() {
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("username");
            String password = request.getParameter("password");
            JsonObject result = Json.createObjectBuilder()
                    .add("success", true)
                    .add("user", name == null ? "NULL" : name)
                    .add("password", password == null ? "NULL" : password)
                    .build();
            response.setContentType("application/json");
            response.getOutputStream().print(result.toString());
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}