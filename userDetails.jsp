<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java_server.GetConnection" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>User Details</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f0f8ff;
      margin: 0;
      padding: 0;
    }
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background-color: #007bff;
      color: white;
      padding: 20px;
    }
    .header h1 {
      margin: 0;
    }
    .header button {
      background-color: #ff4757;
      color: white;
      border: none;
      padding: 10px 20px;
      font-size: 16px;
      border-radius: 5px;
      cursor: pointer;
    }
    .header button:hover {
      background-color: #e43f5a;
    }
    .container {
      width: 95%;
      margin: 20px auto;
      background-color: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
    }
    th, td {
      border: 1px solid #ccc;
      padding: 12px;
      text-align: center;
    }
    th {
      background-color: #007bff;
      color: white;
    }
    tr:nth-child(even) {
      background-color: #f2f2f2;
    }
  </style>
</head>
<body>
  <div class="header">
    <h1>User Details</h1>
    <button onclick="window.history.back()">Back</button>
  </div>
  <div class="container">
    <table>
      <thead>
        <tr>
          <th>ID No</th>
          <th>User ID</th>
          <th>Password</th>
          <th>Post</th>
        </tr>
      </thead>
      <tbody>
        <%
          try {
            Connection con = GetConnection.makeConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT IdNo, UserId, Pass, Job FROM employee");
            while (rs.next()) {
        %>
        <tr>
          <td><%= rs.getString("IdNo") %></td>
          <td><%= rs.getString("UserId") %></td>
          <td>******</td>
          <td><%= rs.getString("Job") %></td>
        </tr>
        <%
            }
            con.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        %>
      </tbody>
    </table>
  </div>
</body>
</html>