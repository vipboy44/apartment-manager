package poly.com.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.com.dto.ResponseDTO;
import poly.com.request.EmployeeRequest;
import poly.com.service.EmployeeService;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeeAPI {

    @Autowired
    EmployeeService employeeService;

    // <-------------------------- findAll ----------------------------->
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll() {
        return employeeService.findAll();
    }

    // <--------------------- findByID -------------------------------->
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable int id) {
        return employeeService.findById(id);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody EmployeeRequest signUpRequest) {
        return employeeService.insertEmployee(signUpRequest);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable int id, @Valid @RequestBody EmployeeRequest employeeRequest) {

        return employeeService.updateEmployee(id, employeeRequest);
    }

    @PutMapping("/reset-password/{id}")
    public ResponseEntity<ResponseDTO> resetPassword(@PathVariable int id) {

        return employeeService.resetPassword(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
        return employeeService.deleteEmployee(id);
    }

    @PostMapping("/upload-file/{id}")
    public ResponseEntity<ResponseDTO> uploadFile(@PathVariable int id,
    											  @RequestParam(name="file", required = false) MultipartFile mFile) throws IOException {
        return employeeService.uploadFile(mFile, id);
    }

    /*  ------------------------- export file excel ----------------*/
    @GetMapping("/export-excel")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
        return employeeService.exportToExcel(response);
    }


}
