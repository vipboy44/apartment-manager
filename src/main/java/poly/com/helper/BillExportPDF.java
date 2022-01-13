package poly.com.helper;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import poly.com.entity.Bill;


public class BillExportPDF {

	private Bill bill;

	public BillExportPDF(Bill bill) {
		this.bill = bill;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		
		cell.setPhrase(new Phrase("Loại"));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Chỉ số cũ"));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Chỉ số mới"));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Số lượng"));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Đơn giá(vnd)"));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Thuế(10%)"));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Thành tiền(vnd)"));
		table.addCell(cell);

		
	}
	
	private void writeTableData(PdfPTable table) {
		
	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException {
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		document.add(new Paragraph("Hóa đơn tháng 10"));
		
		PdfPTable table = new PdfPTable(7);
	
		
		writeTableData(table);
		writeTableHeader(table);
		document.add(table);
		
		document.close();
		
	}
}
