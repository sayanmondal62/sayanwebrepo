<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Medicine Selling List</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f3f4f6;
      margin: 40px;
      color: #333;
    }
    input[type="date"], button {
      padding: 12px 20px;
      margin-right: 10px;
      border: 1px solid #ccc;
      border-radius: 5px;
      font-size: 16px;
    }
    button {
      background-color: #007bff;
      color: white;
      cursor: pointer;
      transition: background-color 0.3s;
    }
    button:hover {
      background-color: #0056b3;
    }
    #goBackBtn {
      background-color: #ff4757;
    }
    #goBackBtn:hover {
      background-color: #e63946;
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
      position: relative;
    }
    th {
      background-color: #007bff;
      color: white;
    }
    .tooltip {
      display: none;
      position: absolute;
      background-color: #fff;
      border: 1px solid #ccc;
      padding: 10px;
      z-index: 10;
      top: 100%;
      left: 50%;
      transform: translateX(-50%);
      width: 300px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    td:hover .tooltip {
      display: block;
    }
  </style>
</head>
<body>
  <h1>Medicine Selling List</h1>
  <label for="dateInput">Select Date:</label>
  <input type="date" id="dateInput" required />
  <button onclick="fetchData()">Show Data</button>
  <button id="goBackBtn" onclick="goBack()">Go Back</button>

  <table id="dataTable">
    <thead>
      <tr>
        <th>Bill No</th>
        <th>Buyer Name</th>
      </tr>
    </thead>
    <tbody></tbody>
  </table>

  <script>
    function fetchData() {
      const selectedDate = document.getElementById('dateInput').value;
      const tableBody = document.querySelector('#dataTable tbody');
      tableBody.innerHTML = '';

      if (!selectedDate) {
        alert('Please select a date.');
        return;
      }

      const xhr = new XMLHttpRequest();
      xhr.open('GET', 'FetchDataServlet?date='+selectedDate, true);
      xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
          tableBody.innerHTML = xhr.responseText;
        }
      };
      xhr.send();
    }

    function goBack() {
      window.history.back();
    }
  </script>
</body>
</html>