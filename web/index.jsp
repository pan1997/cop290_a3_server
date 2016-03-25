<%@ page import="cop290.web.tmpclass" %><%--
  Created by IntelliJ IDEA.
  User: pankaj
  Date: 18/3/16
  Time: 12:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    tmpclass.init();
%>
<html>
    <head>
        <title>IITD Complaint</title>
    </head>
    <style>
        body {
            background-color: #dddddd;
            margin: 0px;
        }
        nav {
            background-color: white;
            height: 25px;
        }
        nav a {
            margin-top: 50px;
        }
    </style>
    <body>
        <nav>
            <a href="index.jsp?login">Login</a>
            <a href="logout">Logout</a>
            <a href="index.jsp?submit">Submit</a>
            <a href="complaints/institute">Institute</a>
            <a href="complaints/individual">Individual</a>
            <a href="complaints/hostel">Hostel</a>
        </nav>
        <%  if(request.getParameter("login")!=null){%>
        <form action="login" method="POST">
            Username:<input type="text" name="username">
            <br/>
            Password:<input type="password" name="password">
            <br/>
            <input type="submit" value="Login">
        </form>
        <%}%>
        <%  if(request.getParameter("submit")!=null){%>
        <form action="/complaints/submit" method="GET">
            Title:<input type="text" name="title">
            <br/>
            Detail:<input type="text" name="detail">
            <br/>
            Level:<input type="text" name="level">
            <br/>
            <input type="submit" value="Submit">
        </form>
        <%}%>
    </body>
</html>