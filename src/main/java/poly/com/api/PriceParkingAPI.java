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
import poly.com.entity.PriceParking;
import poly.com.service.PriceParkingService;

@RestController
@RequestMapping("/api/price-parking")
public class PriceParkingAPI {

// < ----------------------------------- Class PriceParking RestController ---------------------------->
	@Autowired
	PriceParkingService priceParkingService;
	// ------------------------------------------------
	
	
	// <------------------------- findAll --------------------------->
	@GetMapping
	public ResponseEntity<ResponseDTO>  findAll() {
		return priceParkingService.findAll();
	}

	// < ----------------------- findById --------------------------->
	@GetMapping("/{id}")
	public ResponseEntity<ResponseDTO>  findbyId(@PathVariable int id) {
		return priceParkingService.findbyId(id);
	}

	// < ------------------------ Create ----------------------------->
	@PostMapping
	public ResponseEntity<ResponseDTO>  createPriceParking(@Valid @RequestBody PriceParking priceParking) {
		return priceParkingService.createPriceParking(priceParking);
	}

	// < -------------------------- Update ---------------------------->
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDTO>  updatePriceParking(@PathVariable int id,
														  @Valid @RequestBody PriceParking priceParking) {
		return priceParkingService.updatePriceParking(id, priceParking);
	}

	// < -------------------------- Delete --------------------------->
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO>  deletePriceParking(@PathVariable int id) {
		return priceParkingService.deletePriceManagemet(id);
	}

}
