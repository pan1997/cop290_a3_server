package cop290.web;

import javax.json.Json;
import javax.json.JsonObject;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

/**
 * Created by pankaj on 25/3/16.
 */
public class tmpclass {
    static InitialContext initialContext;
    static Random random;
    static DataSource ds;
    private static boolean init=true;
    public static void init(){
        if(init)
        try {
            initialContext=new InitialContext();
            ds=(DataSource)initialContext.lookup("java:/comp/env/jdbc/cop290db");
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public static String randomName(){
        return ""+Math.abs(random.nextInt())+Math.abs(random.nextInt())+Math.abs(random.nextInt())+Math.abs(random.nextInt())+Math.abs(random.nextInt());
    }
    private static final String server="jdbc:mysql://localhost/cop290db";

    static JsonObject getUserJson(String login,String pass)throws SQLException {
        Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement();
        //System.out.println("SELECT * FROM Users WHERE login='" + login + "' AND password='" + pass + "'");
        ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE login='" + login + "' AND password='" + pass + "'");
        JsonObject result;
        if(rs.next()) {
            String ren = rs.getString("entry_number");
            result =
                    Json.createObjectBuilder().add("user_id", rs.getInt("user_id"))
                            .add("first_name", rs.getString("first_name"))
                            .add("last_name", rs.getString("last_name"))
                            .add("entry_number", ren == null ? "" : ren)
                            .add("group_id",rs.getInt("group_id"))
                            .add("department_id",rs.getInt("department_id"))
                            .add("hostel_id",rs.getInt("hostel_id"))
                            .build();
        }else result=null;
        stmt.close();
        conn.close();
        return result;
    }
    static JsonObject getComplaintSummary(ResultSet rs) throws SQLException{
        return Json.createObjectBuilder()
                .add("complaint_id",rs.getInt("complaint_id"))
                .add("user_id",rs.getInt("user_id"))
                .add("title",rs.getString("title"))
                .add("date_submitted",rs.getString("date_submitted"))
                .add("name",rs.getString("first_name")+" "+rs.getString("last_name"))
                .add("discription",rs.getString("discritption"))
                .build();
    }
}
