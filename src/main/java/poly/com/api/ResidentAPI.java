package poly.com.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.com.dto.ResponseDTO;
import poly.com.entity.Resident;
import poly.com.service.ResidentService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/resident")
public class ResidentAPI {
    // < -------------------------------- Class Residential RestController ----------------------------->
    @Autowired
    ResidentService residentService;
    // --------------------------------

    // < ---------------------- findAll ------------------------->
    @GetMapping()
    public ResponseEntity<ResponseDTO> findAll() {
        return residentService.findAll();
    }

    // < ------------------------- findById ---------------------->
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable int id) {
        return residentService.findById(id);
    }

    // find All Id
    @GetMapping("/listid")
    public ResponseEntity<List<String>> findAllId() {
        return residentService.findAllId();
    }

    // < ----------------------------- Create --------------------->
    @PostMapping()
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody Resident resident) {
        return residentService.create(resident);
    }

    // < ----------------------------- Update --------------------->
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable int id, @Valid @RequestBody Resident resident) {
        return residentService.update(id, resident);
    }

    // < ----------------------------- Delete --------------------->
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
        return residentService.delete(id);
    }

    /*  ------------------------- export file excel ----------------*/
    @GetMapping("/export-excel")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
        return residentService.exportToExcel(response);
    }
}
