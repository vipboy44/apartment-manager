package poly.com.helper;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import poly.com.entity.Employee;
import poly.com.entity.Role;

public class EmployeeExportExcel {
    Employee newEmployee = new Employee();
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Employee> employeeList;
    public EmployeeExportExcel(List<Employee> employeeList) {
        this.employeeList = employeeList;
        workbook = new XSSFWorkbook();
        /* create sheet Excel  */
        sheet = workbook.createSheet("Danh sách nhân viên");

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
        cell.setCellValue("ID Nhân viên");
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
        cell.setCellValue("Số CMND");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(4);
        // -------------------------
        cell = row.createCell(5);
        cell.setCellValue("Điện thoại");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(5);
        // -------------------------
        cell = row.createCell(6);
        cell.setCellValue("Email");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(6);
        // -----------------------
        cell = row.createCell(7);
        cell.setCellValue("Địa chỉ");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(7);
        // -------------------------
        cell = row.createCell(8);
        cell.setCellValue("Quyền");
        cell.setCellStyle(cellStyle);
        sheet.autoSizeColumn(8);
    }

    /* - ---------------- write data in row in Excel -----------------  */
    private void writeDataRows() {
     
   
        
        
        int rowcount = 1;    
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        cellStyle.setFont(font);
        for (Employee employee : employeeList) {
            Row row = sheet.createRow(rowcount++);
            // -------------------------
            Cell cell = row.createCell(0);
            cell.setCellValue(employee.getId());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(0);
            // -------------------------
            cell = row.createCell(1);
            cell.setCellValue(employee.getFullName());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(1);
            // -------------------------
            cell = row.createCell(2);
            cell.setCellValue(employee.getGender() ? "Nam" : "Nữ");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(2);
            // -------------------------
            cell = row.createCell(3);
            cell.setCellValue(employee.getBirthday() + "");
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(3);
            // -------------------------
            cell = row.createCell(4);
            cell.setCellValue(employee.getIdentitycard());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(4);
            // -------------------------
            cell = row.createCell(5);
            cell.setCellValue(employee.getPhone());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(5);
            // -------------------------
            cell = row.createCell(6);
            cell.setCellValue(employee.getEmail());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(6);
            // -----------------------
            cell = row.createCell(7);
            cell.setCellValue(employee.getAddress());
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(7);
            // -------------------------
            cell = row.createCell(8);
            cell.setCellValue(getRoleNames(employee));
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

    public String getRoleNames(Employee employee) {   	 	
           Set<Role> roles = employee.getRoles();
           String roleString = "" ;
           for (Role role : roles) {
               roleString += role.getName()+" ";
           }
           
    	return roleString;
    }
    
}
