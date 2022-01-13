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
import poly.com.entity.PriceGarbage;
import poly.com.service.PriceGarbageService;

@RestController
@RequestMapping("/api/price-garbage")
public class PriceGarbageAPI {
	
	// < ----------------------------------- Class PriceGarbage RestController  ---------------------------->
	@Autowired
	PriceGarbageService priceGarbageService;
	// ------------------------------------------------
	
	
	// <------------------------- findAll --------------------------->
	@GetMapping()
	public ResponseEntity<ResponseDTO> findPriceGarbageAll() {
		return priceGarbageService.findPriceGarbageAll();
	}

	// < ----------------------- findById --------------------------->
	@GetMapping("/{id}")
	public ResponseEntity<ResponseDTO> findPriceGarbagebyId(@PathVariable int id) {
		return priceGarbageService.findPriceGarbageById(id);
	}

	// < ------------------------ Create ----------------------------->
	@PostMapping()
	public ResponseEntity<ResponseDTO> createPriceGarbage( @Valid @RequestBody PriceGarbage priceGarbage) {
		return priceGarbageService.createPriceGarbage(priceGarbage);
	}

	// < -------------------------- Update ---------------------------->
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDTO> updatePriceGarbage(@PathVariable int id,
														   @Valid @RequestBody PriceGarbage priceGarbage) {
		return priceGarbageService.updatePriceGarbage(id, priceGarbage);
	}

	// < -------------------------- Delete --------------------------->
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO> deletePriceGarbage(@PathVariable int id) {
		return priceGarbageService.deletePriceGarbage(id);
	}

}
