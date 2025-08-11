<!DOCTYPE html>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java_server.GetConnection"%>
<%@page import="java.sql.Connection"%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Online Medicine Shop</title>
<style>
body {
	font-family: 'Poppins', sans-serif;
	margin: 0;
	padding: 0;
	background-color: #f9fafb;
	color: #333;
}

header {
	background: linear-gradient(135deg, #007bff, #6610f2);
	color: white;
	padding: 20px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

header h1 {
	margin: 0;
}

.header-buttons button {
	background-color: #ff4757;
	border: none;
	color: white;
	padding: 10px 20px;
	margin-left: 10px;
	border-radius: 5px;
	cursor: pointer;
	transition: background 0.3s ease;
}

.header-buttons button:hover {
	background-color: #e84118;
}

.container {
	margin: 40px auto;
	padding: 20px;
	background-color: #fff;
	border-radius: 12px;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
	max-width: 600px;
}

label {
	font-weight: bold;
	margin-top: 10px;
	display: block;
}

input, select {
	padding: 10px;
	border-radius: 8px;
	border: 1px solid #ccc;
	font-size: 16px;
	width: calc(100% - 22px);
	margin-bottom: 10px;
}

input:focus, select:focus {
	border-color: #007bff;
	outline: none;
}

input[readonly] {
	background-color: #f0f0f0;
}

button[type="button"] {
	background-color: #28a745;
	color: white;
	border: none;
	padding: 10px 20px;
	border-radius: 5px;
	cursor: pointer;
	margin-top: 20px;
}

button[type="button"]:hover {
	background-color: #218838;
}

#medicineTable {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

#medicineTable th, #medicineTable td {
	border: 1px solid #ccc;
	padding: 10px;
	text-align: center;
}

#medicineTable th {
	background-color: #007bff;
	color: white;
}

#totalAmount {
	font-weight: bold;
	color: #007bff;
}
</style>
<script>
let totalAmount = 0;
function fetchMedicineDetails() {
  const medicineSelect = document.getElementById("medicineSelect");
  const selectedMedicine = medicineSelect.value;
  document.getElementById("units").value = 0; 

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

function calculateAmount() {
	const price = parseFloat(document.getElementById("price").value);
	const units = parseInt(document.getElementById("units").value);
	const stock = parseInt(document.getElementById("stock").value);

	if (isNaN(units) || units < 0 || units > stock) {
		alert("Please enter a valid number of units within available stock.");
		if(isNaN(units) || units <= 0 )	document.getElementById("units").value = 0;
		if(units > stock) document.getElementById("units").value = stock;
		return;
	}

	const amount = price * units;
	const medicineName = document.getElementById("medicineSelect").value;
	const table = document.getElementById("medicineTable");

	let rowExists = false;
	for (let i = 1; i < table.rows.length; i++) {
		const row = table.rows[i];
		if (row.cells[0].innerText === medicineName) {
			row.cells[1].innerText = units;
			row.cells[2].innerText = amount;
			rowExists = true;
		}
		if(row.cells[1].innerText==0) document.getElementById("medicineTable").deleteRow(i);
	}

	if (!rowExists) {
		const row = table.insertRow();
		row.insertCell(0).innerText = medicineName;
		row.insertCell(1).innerText = units;
		row.insertCell(2).innerText = amount;
	}

	updateTotalAmount();
}

function updateTotalAmount() {
	const table = document.getElementById("medicineTable");
	let total = 0;
	for (let i = 1; i < table.rows.length; i++) {
		total += parseFloat(table.rows[i].cells[2].innerText);
	}
	document.getElementById("totalAmount").value = total;
}

function logout() {
	window.location.href = "login.html";
}

function checkForm() {
    const buyerName = document.getElementById("buyerName").value.trim();
    const table = document.getElementById("medicineTable");

    if (!buyerName) {
        alert("Please enter the buyer's name.");
        return;
    }

    if (table.rows.length <= 1) {
        alert("Please add at least one medicine to the bill.");
        return;
    }

    submitForm(); // Only submits if all checks pass
}


function submitForm() {
  const tableData = [];
  const table = document.getElementById("medicineTable");

  for (let i = 1; i < table.rows.length; i++) {
    const row = table.rows[i];
    tableData.push({
      medicineName: row.cells[0].innerText,
      units: row.cells[1].innerText,
      amount: row.cells[2].innerText
    });
  }

  document.getElementById("hiddenTableData").value = JSON.stringify(tableData);
  document.getElementById("billForm").submit();
}
</script>
</head>
<body>
  <header>
    <h1>Welcome to Online Medicine Shop</h1>
    <div class="header-buttons">
		<button onclick="logout()">Logout</button>
	</div>
  </header>

  <div class="container">
    <form id="billForm" action="GenerateBill" method="post">
      <label for="buyerName">Buyer Name:</label>
      <input type="text" id="buyerName" name="buyerName" required>
      <input type="hidden" id="hiddenTableData" name="tableData">

      <label for="medicineSelect">Select Medicine:</label>
      <select id="medicineSelect" onchange="fetchMedicineDetails()">
        <option value="">Select</option>
        <%
          try (Connection conn = GetConnection.makeConnection();
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery("SELECT madicine_name FROM medicine")) {
            while (rs.next()) {
        %>
        <option value="<%=rs.getString("madicine_name")%>"><%=rs.getString("madicine_name")%></option>
        <%
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        %>
      </select>

      <label for="price">Price:</label>
      <input type="text" id="price" readonly>
      <label for="stock">Stock:</label>
      <input type="text" id="stock" readonly>
      <label for="exp_date">Expiration Date:</label>
      <input type="text" id="exp_date" readonly>
      <label for="units">Units to Purchase:</label>
      <input type="number" id="units" oninput="calculateAmount()">

      <table id="medicineTable">
        <tr>
          <th>Medicine Name</th>
          <th>Units</th>
          <th>Amount</th>
        </tr>
      </table>

      <label for="totalAmount">Total Amount:</label>
      <input type="text" id="totalAmount" readonly>

      <button type="button" onclick="checkForm()">Generate Bill</button>
    </form>
  </div>
</body>
</html>