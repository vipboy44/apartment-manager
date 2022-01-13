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

import poly.com.entity.Resident;

public class ResidentExportExcel {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Resident> residentList;

    public ResidentExportExcel(List<Resident> residentList) {
        this.residentList = residentList;
        workbook = new XSSFWorkbook();
        /* create sheet Excel  */
        sheet = workbook.createSheet("Danh sách cư dân");
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
        cell.setCellValue("ID Cư dân");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(0);
        // -------------------------
        cell = row.createCell(1);
        cell.setCellValue("Họ và Tên");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(1);
        // -------------------------
        cell = row.createCell(2);
        cell.setCellValue("Giới tính");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(2);
        // -------------------------
        cell = row.createCell(3);
        cell.setCellValue("Ngày sinh");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(3);
        // -------------------------
        cell = row.createCell(4);
        cell.setCellValue("Quê quán");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(4);
        // -------------------------
        cell = row.createCell(5);
        cell.setCellValue("Nghề nghiệp");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(5);
        // -------------------------
        cell = row.createCell(6);
        cell.setCellValue("Điện thoại");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(6);
        // -------------------------
        cell = row.createCell(7);
        cell.setCellValue("Email");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(7);
        // -------------------------
        cell = row.createCell(8);
        cell.setCellValue("Số CMND");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(8);
        // -------------------------
        cell = row.createCell(9);
        cell.setCellValue("ID căn hộ");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(9);
    }

    /* - ---------------- write data in row in Excel -----------------  */
    private void writeDataRows() {
        int rowcount = 1;
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        cellStyle.setFont(font);
        for (Resident resident : residentList) {
            Row row = sheet.createRow(rowcount++);
            // -------------------------
            Cell cell = row.createCell(0);
            cell.setCellValue(resident.getId());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(0);
            // -------------------------
            cell = row.createCell(1);
            cell.setCellValue(resident.getFullname());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(1);
            // -------------------------
            cell = row.createCell(2);
            cell.setCellValue(resident.getGender() ? "Nam" : "Nữ");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(2);
            // -------------------------
            cell = row.createCell(3);
            cell.setCellValue(resident.getBirthday() + "");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(3);
            // -------------------------
            cell = row.createCell(4);
            cell.setCellValue(resident.getHometown());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(4);
            // -------------------------
            cell = row.createCell(5);
            cell.setCellValue(resident.getJob());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(5);
            // -------------------------
            cell = row.createCell(6);
            cell.setCellValue(resident.getPhone());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(6);
            // -------------------------
            cell = row.createCell(7);
            cell.setCellValue(resident.getEmail());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(7);
            // -------------------------
            cell = row.createCell(8);
            cell.setCellValue(resident.getIdentitycard());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(8);
            // -------------------------
            cell = row.createCell(9);
            cell.setCellValue(resident.getApartment().getId());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(9);
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