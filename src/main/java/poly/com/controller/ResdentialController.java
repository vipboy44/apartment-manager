package poly.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.com.repository.TypeVehicelRepository;


@Controller
@RequestMapping("/quan-ly")
public class ResdentialController {
    @Autowired
    TypeVehicelRepository typeVehicelRepository;
    // return template page table residential
    @GetMapping("/cu-dan")
    public String pageTableCudan( ModelMap model) {
        model.addAttribute("TypeVehicles", typeVehicelRepository.findAll());
        return "quanly/cu-dan/table-cu-dan";
    }

    @GetMapping("/xe")
    public String pageTableXe( ModelMap model) {
        model.addAttribute("TypeVehicles", typeVehicelRepository.findAll());
        return "quanly/cu-dan/table-xe";
    }


}
