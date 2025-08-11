<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java_server.GetConnection" %>

<!DOCTYPE html>
<html>
<head>
    <title>Out of Stock Medicines</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f3f4f6;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            margin: 50px auto;
        }
        h1 {
            color: #e74c3c;
        }
        .medicine-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            background-color: #fff;
            border: 1px solid #ccc;
            margin-top: 10px;
            border-radius: 5px;
        }
        a, button {
            text-decoration: none;
            color: #fff;
            background-color: #007bff;
            padding: 10px 20px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
        }
        button {
            background-color: #e74c3c;
        }
    </style>
  	<script>
        function removeMedicine(medicineName, element) {
            if (confirm('Are you sure you want to remove ' + medicineName + '?')) {
                const xhr = new XMLHttpRequest();
                xhr.open('GET', 'RemoveexpServlet?name=' + encodeURIComponent(medicineName), true);
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        alert('Medicine removed successfully!');
                        element.parentElement.remove(); // Remove the item from the list
                        setTimeout(() => location.reload(), 10); // Auto refresh after 1 second
                    }
                };
                xhr.send();
            }
        }
        
        function autoRefresh() {
            setInterval(() => location.reload(), 5000); // Auto refresh every 5 seconds
        }
        autoRefresh() 
    </script>
</head>
<body>
    <div class="container">
        <h1>Expired Medicines</h1>
        <%
            try {
                Connection con = GetConnection.makeConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name FROM expmedicines");
                
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    String medicineName = rs.getString("name");
        %>
        <div class="medicine-item">
            <span><%= medicineName %></span>
            <button onclick="removeMedicine('<%= medicineName %>')">Remove</button>
        </div>
        <%
                }
                if (!hasData) {
        %>
        <p>No Medicines are Expired!</p>
        <%
                }
                con.close();
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
        %>
        <br>
        <a href="Admin.html">Back to Admin Panel</a>
    </div>
</body>
</html>