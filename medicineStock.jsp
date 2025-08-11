<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@page
	import="org.apache.tomcat.jdbc.pool.interceptor.AbstractCreateStatementInterceptor"%>
<%@page import="java_server.GetConnection"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Medicine Stock</title>
<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #e9f5ff;
  color: #333;
}

header {
  background: linear-gradient(135deg, #007bff, #6610f2);
  color: white;
  padding: 20px;
  text-align: center;
  font-size: 28px;
  font-weight: bold;
}

.container {
  margin: 40px auto;
  width: 80%;
  background-color: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

label {
  display: block;
  margin-top: 15px;
  font-size: 16px;
  font-weight: bold;
}

input, select {
  width: 100%;
  padding: 10px;
  margin-top: 8px;
  margin-bottom: 20px;
  border-radius: 8px;
  border: 1px solid #ccc;
  box-sizing: border-box;
  font-size: 16px;
}

select {
  cursor: pointer;
}

input[readonly] {
  background-color: #f3f3f3;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 15px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #0056b3;
}
</style>
<script>
function fetchMedicineDetails() {
  const medicineSelect = document.getElementById("medicineSelect");
  const selectedMedicine = medicineSelect.value;
  if (!selectedMedicine) return;

  const xhr = new XMLHttpRequest();
  xhr.open("GET", "FetchMedicineDetails?medicineName=" + encodeURIComponent(selectedMedicine), true);
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const obj = JSON.parse(xhr.responseText);
      document.getElementById("price").value = obj.price;
      document.getElementById("stock").value = obj.stock;
      document.getElementById("exp_date").value = obj.exp_date;
    }
  };
  xhr.send();
}

function goBack() {
	  window.history.back();
	}
</script>
</head>
<body>
<header>Medicine Stock</header>
<div class="container">
  <label for="medicineSelect">Select Medicine:</label>
  <select id="medicineSelect" onchange="fetchMedicineDetails()">
    <option value="">Select a Medicine</option>
    <% Connection con = null; Statement stmt = null; ResultSet rs = null;
    try {
      con = GetConnection.makeConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery("SELECT madicine_name FROM medicine");
      while (rs.next()) {
        String medicineName = rs.getString("madicine_name"); %>
        <option value="<%=medicineName%>"><%=medicineName%></option>
    <% } } catch (Exception e) { e.printStackTrace();
    } finally {
      if (con != null) try { con.close(); } catch (Exception e) {}
    } %>
  </select>

  <label for="price">Price (Rs):</label>
  <input type="text" id="price" readonly>

  <label for="stock">Stock:</label>
  <input type="text" id="stock" readonly>

  <label for="exp_date">Expiration Date:</label>
  <input type="text" id="exp_date" readonly>
  
  <button onclick="goBack()">Go Back</button>
</div>
</body>
</html>