package java_server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@WebServlet("/GenerateBill")
public class GenerateBill extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int billNo=0;
		try {
			billNo=GetId.billNo();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		ArrayList<String> medicineName = new ArrayList<String>();
		ArrayList<String> units = new ArrayList<String>();
		ArrayList<String> amount = new ArrayList<String>();
		String buyerName = request.getParameter("buyerName");
		String tableDataJson = request.getParameter("tableData");
		unpack(tableDataJson,medicineName,units,amount);
		pushInDatabase(buyerName,billNo, medicineName, units, amount);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=BillSummary.pdf");

		try (ServletOutputStream out = response.getOutputStream()) {
			generatePdf(out,buyerName,billNo,medicineName,units,amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			for(int i=0;i<medicineName.size();i++)
			{
				Updatemedicinetable.update(medicineName.get(i),units.get(i));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	static void unpack(String jsonobj,ArrayList<String> medicineName, ArrayList<String> units, ArrayList<String> amount) {
		jsonobj = jsonobj.substring(1, jsonobj.length() - 1);
		String[] objects = jsonobj.split("\\},\\{");
		for (String obj : objects) {
			obj = obj.replaceAll("[{}\"]", "");
			String[] pairs = obj.split(",");
			for (String pair : pairs) {
				String[] keyValue = pair.split(":");
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();

				if (key.equals("medicineName"))
					medicineName.add(value);
				else if (key.equals("units"))
					units.add(value);
				else if (key.equals("amount"))
					amount.add(value);
			}
		}
	}

	public static void generatePdf(ServletOutputStream out, String buyerName, int billNo, ArrayList<String> medicineName, ArrayList<String> units, ArrayList<String> amount) throws DocumentException, IOException {
	    Document document = new Document();
	    PdfWriter.getInstance(document, out);
	    document.open();

	    // Title
	    Font titleFont = new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD, new BaseColor(0, 123, 255));
	    Paragraph title = new Paragraph("My Medicine Shop", titleFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    document.add(title);
	    document.add(new Paragraph("\n"));

	    // Bill No (Right Side)
	    Font billFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	    Paragraph billInfo = new Paragraph("Bill No: " + billNo+1, billFont);
	    billInfo.setAlignment(Element.ALIGN_RIGHT);
	    document.add(billInfo);

	    // Buyer Details
	    String buyingDate = LocalDate.now().toString();
	    document.add(new Paragraph("Buyer Name: " + buyerName));
	    document.add(new Paragraph("Buying Date: " + buyingDate));
	    document.add(new Paragraph("\n"));

	    // Table
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100);
	    addTableHeader(table, "Medicine Name", "Quantity", "Amount");

	    int totalAmount = 0;
	    for (int i = 0; i < medicineName.size(); i++) {
	        table.addCell(medicineName.get(i));
	        table.addCell(units.get(i));
	        table.addCell("Rs." + amount.get(i));
	        totalAmount += Integer.parseInt(amount.get(i));
	    }

	    document.add(table);

	    // Total Amount
	    Paragraph total = new Paragraph(" Total Amount: " + totalAmount,
	            new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(40, 167, 69)));
	    total.setAlignment(Element.ALIGN_RIGHT);
	    document.add(total);

	    document.close();

	    // Save the PDF with Bill No as Filename
	    try (FileOutputStream fos = new FileOutputStream("bill_" + billNo + ".pdf")) {
	        Document fileDocument = new Document();
	        PdfWriter.getInstance(fileDocument, fos);
	        fileDocument.open();
	        fileDocument.add(title);
	        fileDocument.add(billInfo);
	        fileDocument.add(new Paragraph("Buyer Name: " + buyerName));
	        fileDocument.add(new Paragraph("Buying Date: " + buyingDate));
	        fileDocument.add(table);
	        fileDocument.add(total);
	        fileDocument.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private static void addTableHeader(PdfPTable table, String... headers) {
	    for (String header : headers) {
	        PdfPCell cell = new PdfPCell(new Phrase(header));
	        cell.setBackgroundColor(new BaseColor(0, 123, 255));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setPadding(10);
	        cell.setPhrase(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE)));
	        table.addCell(cell);
	    }
	}

	
	static void pushInDatabase(String Name,int billNo,ArrayList<String> medicineName, ArrayList<String> units, ArrayList<String> amount) {
		String medicinelist=String.join(",",medicineName);
		String unit=String.join(",", units);
		String amt=String.join(",",amount);
		LocalDate currentDate = LocalDate.now();
		java.sql.Date buyingDate = java.sql.Date.valueOf(currentDate);
		
		try {
			Connection conn=GetConnection.makeConnection();
			String query = "INSERT INTO Bills (BillNo,BuyerName, MedicineNames, Quantities, Amount, BuyingDate) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, billNo+1);
            pstmt.setString(2, Name);
            pstmt.setString(3, medicinelist);
            pstmt.setString(4, unit);
            pstmt.setString(5, amt);
            pstmt.setDate(6, buyingDate);

            // Executing the query
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            }
            pstmt.close();
            conn.close();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
