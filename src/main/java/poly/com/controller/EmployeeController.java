package poly.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quan-ly/nhan-vien")
public class EmployeeController {

    @GetMapping()
    public String pageEmployee(){
        return "quanly/nhan-vien/table-nhan-vien";
    }
    
    // return template page chu can ho
 	@GetMapping("/{id}")
 	public String formUpdate(@PathVariable int id, ModelMap model) {
 			model.addAttribute("idEmployee" , id);
 		return "quanly/nhan-vien/form-update-nhanvien";
 	}
    
    
    
}
