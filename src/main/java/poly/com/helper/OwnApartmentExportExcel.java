package poly.com.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import poly.com.entity.OwnApartment;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class OwnApartmentExportExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<OwnApartment> ownApartmentList;
    public OwnApartmentExportExcel(List<OwnApartment> apartmentList){
        this.ownApartmentList = apartmentList;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Danh sách chủ căn hộ ");
    }

    /* --------------  add name columns table Excel -----------*/
    private  void writeHeaderRow(){
        Row newRow =  sheet.createRow(0);
        CellStyle cellStyle =  workbook.createCellStyle();
        XSSFFont  font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        cellStyle.setFont(font);
        // -------------------------
        Cell cell = newRow.createCell(0);
        cell.setCellValue("ID chủ hộ");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(0);
        // -------------------------
        cell = newRow.createCell(1);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(1);
        // ----------------------------
        cell = newRow.createCell(2);
        cell.setCellValue("Ngày sinh");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(2);
        // ------------------------
        cell = newRow.createCell(3);
        cell.setCellValue("Giới tính");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(3);
        // --------------------------
        cell = newRow.createCell(4);
        cell.setCellValue("Nghề nghiệp");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(4);
        // -----------------------------
        cell = newRow.createCell(5);
        cell.setCellValue("Điện thoại");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(5);
        // ------------------------
        cell = newRow.createCell(6);
        cell.setCellValue("Email");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(6);
        //-----------------------
        cell = newRow.createCell(7);
        cell.setCellValue("Quê quán");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(7);
        //-------------------------
        cell = newRow.createCell(8);
        cell.setCellValue("Số chứng minh");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(8);
    }

    private void writeDataRows() {
        int rowcount = 1;
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        cellStyle.setFont(font);
        for(OwnApartment ownApartment : ownApartmentList){

            Row row = sheet.createRow(rowcount++);
            Cell cell = row.createCell(0);
            cell.setCellValue(ownApartment.getId());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellValue(ownApartment.getFullname());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(1);

            cell = row.createCell(2);
            cell.setCellValue(ownApartment.getBirthday() + "");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(2);

            cell = row.createCell(3);
            cell.setCellValue(ownApartment.getGender() ? "Nam" : "Nữ");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(3);

            cell = row.createCell(4);
            cell.setCellValue(ownApartment.getJob());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(4);

            cell = row.createCell(5);
            cell.setCellValue(ownApartment.getPhone());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(5);

            cell = row.createCell(6);
            cell.setCellValue(ownApartment.getEmail());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(6);

            cell = row.createCell(7);
            cell.setCellValue(ownApartment.getHomeTown());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(7);

            cell = row.createCell(8);
            cell.setCellValue(ownApartment.getIdentitycard());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(8);

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
