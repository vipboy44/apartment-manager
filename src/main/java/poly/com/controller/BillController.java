package poly.com.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.com.entity.Bill;
import poly.com.helper.BillExportExcel;
import poly.com.repository.BillRepository;

@Controller
@RequestMapping("/quan-ly/hoa-don")
public class BillController {

	@Autowired
	BillRepository billRepository;
	
	
	 // return template page table hoa don
    @GetMapping()
    public String pageTableHoadon() {
        return "quanly/hoa-don/table-hoa-don";
    }
    
    
    @GetMapping("/{id}")
    public String pageUpdate(@PathVariable int id, ModelMap model){
    	
    	model.addAttribute("id", id);
        return "quanly/hoa-don/hoa-don-chi-tiet";
    }
    
      /*  ------------------------- export file excel ----------------*/
    @GetMapping("/export-excel/month")
    public void exportToExcel(HttpServletResponse response, @RequestParam("month") int month, @RequestParam("year") int year) throws Exception {

              response.setContentType("application/octet-stream");
              String headerKey = "Content-Disposition";
              String fileName =  "bills-" + month + "-" + year + ".xlsx";
              String headerValue = "attachement; filename = " +fileName;
              response.setHeader(headerKey, headerValue);
              List<Bill> bills = billRepository.findByMonth(month, year);
        
              BillExportExcel billExportExcel = new BillExportExcel(bills);
              billExportExcel.export(response);
    }
    
    @GetMapping("/export-excel/all")
    public void exportToExcel(HttpServletResponse response) throws Exception {;
              response.setContentType("application/octet-stream");
              String headerKey = "Content-Disposition";
              String headerValue = "attachement; filename = bills.xlsx";
              response.setHeader(headerKey, headerValue);
              List<Bill> bills = billRepository.findAll();
        
              BillExportExcel billExportExcel = new BillExportExcel(bills);
              billExportExcel.export(response);
    }
    
    /*
    @GetMapping("/export-pdf")
    public void exportPdf(HttpServletResponse response, @RequestParam("id") int id) throws DocumentException, IOException{ 	
    	response.setContentType("application/pdf; charset=utf-8");
          response.setCharacterEncoding("UTF-8");
    	 String headerKey = "Content-Disposition";
         String headerValue = "attachement; filename=bill.pdf";
         response.setHeader(headerKey, headerValue);
         Bill bill =  billRepository.findById(id).orElse(null);
    	 BillExportPDF  billPDF = new BillExportPDF(bill);
    	 billPDF.export(response);
    }
    */
    
    @GetMapping("/chua-thanh-toan")
    public String DanhSachHoaDonChuaThanhToan() {
    	
        return "quanly/hoa-don/chua-thanh-toan/danh-sach";
    }
    
    @GetMapping("/chua-thanh-toan/{id}")
    public String HoaDonChuaThanhToan(@PathVariable int id, ModelMap model){
    	
    	model.addAttribute("id", id);
        return "quanly/hoa-don/chua-thanh-toan/hoa-don-chi-tiet";
    }
    
    @GetMapping("/chua-thanh-toan/export-excel/all")
    public void exportExcelPaid(HttpServletResponse response) throws Exception {;
              response.setContentType("application/octet-stream");
              String headerKey = "Content-Disposition";
              String headerValue = "attachement; filename = bills.xlsx";
              response.setHeader(headerKey, headerValue);
              List<Bill> bills = billRepository.findByPaid(false);
        
              BillExportExcel billExportExcel = new BillExportExcel(bills);
              billExportExcel.export(response);
    }
    
    
    
}
