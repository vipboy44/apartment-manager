package poly.com.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.com.dto.ResponseDTO;
import poly.com.entity.Vehicle;
import poly.com.service.VehicleService;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleAPI {

    // < --------------------------------- Class RestController Vehicle ----------------------->
    @Autowired
    VehicleService vehicleService;
    // ----------------------------------------

    // < ------------------------ findAll ------------------>
    @GetMapping()
    public ResponseEntity<ResponseDTO> findAll() {
        return vehicleService.findAll();
    }

    // < ------------------------- findById ---------------------->
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable int id) {
        return vehicleService.findById(id);
    }

    // < ----------------------------- Create --------------------->
    @PostMapping()
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody Vehicle vehicle) {
        return vehicleService.create(vehicle);
    }

    // < ----------------------------- Update --------------------->
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable int id, @Valid @RequestBody Vehicle vehicle) {
        return vehicleService.update(id, vehicle);
    }

    // < ----------------------------- Delete --------------------->
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
        return vehicleService.delete(id);
    }

}
