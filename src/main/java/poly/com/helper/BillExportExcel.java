package poly.com.helper;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import poly.com.entity.Bill;

public class BillExportExcel {


    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Bill> bills;

    public BillExportExcel(List<Bill> bills) {
        this.bills = bills;
        workbook = new XSSFWorkbook();
        /* create sheet Excel  */
        sheet = workbook.createSheet("Danh sách hóa đơn");
    }

    /* --------------  add name columns table Excel -----------*/
    private void writeHeaderRow() {
        Row row = sheet.createRow(0);
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        cellStyle.setFont(font);
       // -------------------------
        Cell cell = row.createCell(0);
        cell.setCellValue("Mã hóa đơn");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(0);
        // -------------------------
        cell = row.createCell(1);
        cell.setCellValue("Căn hộ");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(1);
        // -------------------------
        cell = row.createCell(2);
        cell.setCellValue("Ngày tạo");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(2);
        // -------------------------
        cell = row.createCell(3);
        cell.setCellValue("Số điện");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(3);
        // -------------------------
        cell = row.createCell(4);
        cell.setCellValue("Tiền điện(+10% thuế)");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(4);
        // -------------------------
        cell = row.createCell(5);
        cell.setCellValue("Số nước");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(5);
        // -------------------------
        cell = row.createCell(6);
        cell.setCellValue("Tiền nước");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(6);
        // -------------------------
        cell = row.createCell(7);
        cell.setCellValue("Phí quản lý");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(7);
        // -------------------------
        cell = row.createCell(8);
        cell.setCellValue("Phí thu - gom rác");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(8);    
        // -------------------------
        cell = row.createCell(9);
        cell.setCellValue("Xe đạp");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(9);  
        // -------------------------
        cell = row.createCell(10);
        cell.setCellValue("Xe máy");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(10);
        // -------------------------
        cell = row.createCell(11);
        cell.setCellValue("Xe ô tô");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(11);
        // -------------------------
        cell = row.createCell(12);
        cell.setCellValue("Phí gửi xe");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(12);
        // -------------------------
        cell = row.createCell(13);
        cell.setCellValue("Tổng tiền");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(13);
        // -------------------------
        cell = row.createCell(14);
        cell.setCellValue("Đã thanh toán");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(14);
        // -------------------------
        cell = row.createCell(15);
        cell.setCellValue("Người tạo");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(15);
    }

    /* - ---------------- write data in row in Excel -----------------  */
    private void writeDataRows() {
        int rowcount = 1;
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        cellStyle.setFont(font);
        for (Bill bill : bills) {
            Row row = sheet.createRow(rowcount++);
            // -------------------------
            Cell cell = row.createCell(0);
            cell.setCellValue(bill.getId());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(0);
            // -------------------------
            cell = row.createCell(1);
            cell.setCellValue(bill.getApartmentIndex().getApartment().getId());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(1);
            // -------------------------
            cell = row.createCell(2);
            cell.setCellValue(bill.getApartmentIndex().getDate()+"");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(2);
            // -------------------------
            cell = row.createCell(3);
            cell.setCellValue(bill.getElectricityNumber());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(3);
            // -------------------------
            cell = row.createCell(4);
            cell.setCellValue(bill.getElectricityPriceTotal());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(4);
            // -------------------------
            cell = row.createCell(5);
            cell.setCellValue(bill.getWaterNumber());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(5);
            // -------------------------
            cell = row.createCell(6);
            cell.setCellValue(bill.getWaterPrice());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(6);
            // -------------------------
            cell = row.createCell(7);
            cell.setCellValue(bill.getManagementPrice());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(7);
            // -------------------------
            cell = row.createCell(8);
            cell.setCellValue(bill.getGarbagesPrice());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(8);
            // -------------------------
            cell = row.createCell(9);
            cell.setCellValue(bill.getBicycleNumber());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(9);  
            // -------------------------
            cell = row.createCell(10);
            cell.setCellValue(bill.getMotocycleNumber());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(10);
            // -------------------------
            cell = row.createCell(11);
            cell.setCellValue(bill.getCarNumber());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(11);
            // -------------------------
            cell = row.createCell(12);
            cell.setCellValue(bill.getParkingPriceTotal());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(12);
            // -------------------------
            cell = row.createCell(13);
            cell.setCellValue(bill.getTotalPrice());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(13);
            // -------------------------
            cell = row.createCell(14);
            cell.setCellValue(bill.getPaid()?"Đã thanh toán":"Chưa thanh toán");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(14);
            // -------------------------
            cell = row.createCell(15);
            cell.setCellValue(bill.getApartmentIndex().getEmployee().getId());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(15);
        }
    }

    public void export(HttpServletResponse response) throws Exception {
        writeHeaderRow();
        writeDataRows();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
	
	
}
