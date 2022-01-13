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
import poly.com.entity.PriceElectricity;
import poly.com.service.PriceElectricityService;

@RestController
@RequestMapping("/api/price-electricity")
public class PriceElectricityAPI {

// < ----------------------------------- Class PriceElectricity RestController ---------------------------->
	@Autowired
	PriceElectricityService priceElectricityService;
// ------------------------------------------------

	// <------------------------- findAll --------------------------->
	@GetMapping()
	public ResponseEntity<ResponseDTO> findPriceElectricityAll() {
		return priceElectricityService.findAllElectricity();
	}

	// < ----------------------- findById --------------------------->
	@GetMapping("/{id}")
	public ResponseEntity<ResponseDTO> findPriceElectricitybyId(@PathVariable int id) {
		return priceElectricityService.findPriceElectricitybyId(id);
	}

	// < ------------------------ Create ----------------------------->
	@PostMapping()
	public ResponseEntity<ResponseDTO> createPriceElectricity(@Valid @RequestBody PriceElectricity priceElectricity) {
		return priceElectricityService.createPriceElectricity(priceElectricity);
	}

	// < -------------------------- Update ---------------------------->
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDTO> updatePriceElectricity(@PathVariable int id,
			 													  @Valid @RequestBody PriceElectricity priceElectricity) {
		return priceElectricityService.updatePriceElectricity(id, priceElectricity);
	}

	// < -------------------------- Delete --------------------------->
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO> deletePriceElectricity(@PathVariable int id) {
		return priceElectricityService.deletePriceElectricity(id);
	}

}
