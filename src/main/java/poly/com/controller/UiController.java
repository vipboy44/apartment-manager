package poly.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quan-ly")
public class UiController {

	@GetMapping()
    public String pageadmin(Model model) {
        model.addAttribute("trangchu","active");
        return "layout-admin";
    }
	
    // return template page table thông báo
    @GetMapping("/noi-quy")
    public String pageRegulation() {
        return "quanly/noi-quy/noiquy";
    }

    // return template page table thông báo
    @GetMapping("/thong-bao")
    public String pageNotification() {
        return "quanly/thong-bao/table-thong-bao";
    }

    // return template page form table thông báo
    @GetMapping("/thong-bao/{id}")
    public String pageFormNotification(@PathVariable int id, ModelMap model) {
        model.addAttribute("id", id);
        return "quanly/thong-bao/form-thong-bao";
    }
    
    @GetMapping("/thong-tin-tai-khoan")
    public  String pageFormAccount(){
        return  "account/form-thong-tin-tai-khoan";
    }


}
