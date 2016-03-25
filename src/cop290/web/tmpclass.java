package cop290.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by pankaj on 25/3/16.
 */
public class tmpclass {
    private static final String dbClassName="com.mysql.jdbc.Driver";
    private static final String server="jdbc:mysql://localhost/cop290db";
    public static void main(String argv[])throws Exception{
        System.out.println(dbClassName);
        Class.forName(dbClassName);
        Properties p=new Properties();
        p.put("user","cop290");
        p.put("password","cop290@pankaj");
        Connection c= DriverManager.getConnection(server,p);
        System.out.println("Working");
        c.close();
    }
}
