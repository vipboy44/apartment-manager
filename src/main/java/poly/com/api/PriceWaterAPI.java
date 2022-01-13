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
import poly.com.entity.PriceWater;
import poly.com.service.WaterPriceService;

@RestController
@RequestMapping("/api/price-water")
public class PriceWaterAPI {
// < ----------------------------------- Class PriceElectricity RestController ---------------------------->

	@Autowired
	WaterPriceService waterPriceService;

	// ------------------------------------------------
	@GetMapping()
	public ResponseEntity<ResponseDTO> findAll() {	
		return waterPriceService.findAll();
	}

	// < ----------------------- findById --------------------------->
	@GetMapping("/{id}")
	public ResponseEntity<ResponseDTO> findById(@PathVariable int id) {
		return waterPriceService.findById(id);
	}

	// < ------------------------ Create ----------------------------->
	@PostMapping()
	public ResponseEntity<ResponseDTO> createPriceWater(@Valid @RequestBody PriceWater priceWater) {
		return waterPriceService.createPriceWater(priceWater);
	}

	// < -------------------------- Update ---------------------------->
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDTO> updatePriceWater( @PathVariable int id, @Valid @RequestBody PriceWater priceWater) {
		return waterPriceService.updatePriceWaterEntity(id, priceWater);
	}

	// < -------------------------- Delete --------------------------->
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO> deletePriceWater(@PathVariable int id) {
		return waterPriceService.deletePriceWater(id);
	}
}
