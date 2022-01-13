package poly.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.com.repository.TypeVehicelRepository;

@Controller
@RequestMapping("/quan-ly/bang-gia")
public class PriceController {

    @Autowired
    TypeVehicelRepository typeVehicelRepository;

    // return template page table phi rac
    @RequestMapping("/phi-rac")
    public String pageTablePhirac() {
        return "quanly/bang-gia/table-phi-rac";
    }

    // return template page table gia dien 
    @RequestMapping("/gia-dien")
    public String pageTableGiadien() {
        return "quanly/bang-gia/table-gia-dien";
    }

    // return template page table gia nuoc
    @RequestMapping("/gia-nuoc")
    public String pageTableGianuoc() {
        return "quanly/bang-gia/table-gia-nuoc";
    }

    // return template page table phi quan ly 
    @RequestMapping("/phi-quan-ly")
    public String pageTablePhiquanly() {
        return "quanly/bang-gia/table-phi-quan-ly";
    }

    @GetMapping("/phi-gui-xe")
    public String pageTableXe(ModelMap model) {
        model.addAttribute("TypeVehicles", typeVehicelRepository.findAll());
        return "quanly/bang-gia/table-phi-xe";
    }

}
