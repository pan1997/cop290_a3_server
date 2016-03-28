<%@ page import="cop290.web.tmpclass" %>
<%@ page import="javax.json.JsonObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    tmpclass.init();
    request.getSession().setAttribute("web",true);
%>
<html>
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style.css">
    <script src="script1.js"></script>
    <link rel="stylesheet" href="styles.css">
    <title>IITD Complaint</title>
</head>
<body>
<div id='cssmenu'>
    <ul>
        <li><a href="index.jsp">Home</a></li>
        <% JsonObject user=(JsonObject)request.getSession().getAttribute("user");
            if(user!=null){%>
        <li><a href="logout"><%=user.getString("first_name")%></a></li>
        <li><a href="logout">Logout</a></li>
        <%}else{%>
        <li><a href="index.jsp?login">Login</a></li>
        <%}%>
        <li><a href="index.jsp?submit">Submit</a></li>
        <li><a href="index.jsp?content=institute">Institute</a></li>
        <li><a href="index.jsp?content=individual">Individual</a></li>
        <li><a href="index.jsp?content=hostel">Hostel</a></li>
        <li><a href="index.jsp?search">Search</a></li>
    </ul>
</div>
<div class="wrapper">
    <div class="container">
        <%if(request.getParameter("content")==null){%>
        <%  if(request.getParameter("login")!=null){%>
        <h1>Login</h1>
        <form class="form" action="login" method="POST" onsubmit="window.location.href='index.jsp';return true;">
            <input type="text" placeholder="Username" id="username" name="username" value=""/>
            <input type="password" placeholder="Password" id="password" name="password" value=""/>
            <button type="submit" id="login">Login</button>
        </form>
        <%  }else if(request.getParameter("submit")!=null){%>
        <form action="/complaints/submit" method="GET">
            Title:<input type="text" name="title">
            Detail:<input type="text" name="detail">
            Level:<input type="text" name="level">
            <button type="submit" id="submit">Submit</button>
        </form>
        <%}else if(request.getParameter("search")!=null){%>
        <h1>Search for Complaints</h1>
        <form action="/search" method="GET">
            Search:<input type="text" name="query">
            <button type="search" id="search">Search</button>
        </form>
        <%}else{%>
        <h1>Welcome to IITD Complaints Resolving Cell</h1>
        <%}}else{%>
        <iframe src="complaints/<%=request.getParameter("content")%>">
        </iframe>
        <%}%>
    </div>

    <ul class="bg-bubbles">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
    </ul>
</div>

</body>
</html>