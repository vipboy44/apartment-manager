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
import poly.com.entity.Apartment;
import poly.com.service.ApartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/apartment")
public class ApartmentAPI {

    @Autowired
    ApartmentService apartmentService;

    // <------------------------- findAll --------------------------->
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll() {

        return apartmentService.findAll();
    }

    /*  -----------------------  Find All id ---------------------- */
    @GetMapping("/listid")
    public ResponseEntity<List<String>> findAllId() {
        return apartmentService.findAllId();
    }

    // < ----------------------- findById --------------------------->
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findbyId(@PathVariable String id) {
        return apartmentService.findById(id);
    }

    // < ------------------------ Create ----------------------------->
    @PostMapping
    public ResponseEntity<ResponseDTO> createPriceManagement(@Valid @RequestBody Apartment apartment) {
        return apartmentService.createApartment(apartment);
    }

    // < -------------------------- Update ---------------------------->
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updatePriceManagement(@PathVariable String id,
                                                             @Valid @RequestBody Apartment apartment) {
        return apartmentService.updateApartment(id, apartment);
    }

    @PutMapping("/reset-password/{id}")
    public ResponseEntity<ResponseDTO> resetPassword(@PathVariable String id) {

        return apartmentService.resetPassword(id);
    }

    // < -------------------------- Delete --------------------------->
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deletePricemanagement(@PathVariable String id) {
        return apartmentService.deleteApartment(id);

    }
}
