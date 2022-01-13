package poly.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quan-ly/chu-can-ho")
public class OwnApartmetController {

	// return template page table chu can ho
	@GetMapping()
	public String pageTabelChuCanho() {
		return "quanly/chu-ho/table-chu-can-ho";
	}

   // return template page chu can ho
	@GetMapping("/{id}")
	public String formUpdate(@PathVariable int id, ModelMap model) {
		
		 model.addAttribute("idOwn", id);
		return "contents/quanly/chucanho/form-update";
	}
	
	

}
