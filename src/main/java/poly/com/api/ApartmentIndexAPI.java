package poly.com.api;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.com.dto.ResponseDTO;
import poly.com.request.CreateIndexRequest;
import poly.com.request.UpdateIndexRequest;
import poly.com.service.ApartmentIndexService;

@RestController
@RequestMapping("/api/apartment-index")
public class ApartmentIndexAPI {

	@Autowired
	ApartmentIndexService apartmentIndexService;

	@GetMapping
	public ResponseEntity<ResponseDTO> findAll(){
		
		return  apartmentIndexService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseDTO> findAll(@PathVariable int id){
		
		return  apartmentIndexService.findById(id);
	}
	
	@GetMapping("/paid/{isPaid}")
	public ResponseEntity<ResponseDTO> findByPaid(@PathVariable boolean isPaid){
		
		return  apartmentIndexService.findByPaid(isPaid);
	}
	
	@GetMapping("/all-month/{year}")
	public ResponseEntity<List<Integer>> findAllMonth(@PathVariable int year){
		
		return  apartmentIndexService.findAllMonth(year);
	}
	
	@GetMapping("/all-year")
	public ResponseEntity<List<Integer>> findAllYear(){
		
		return  apartmentIndexService.findAllYear();
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody CreateIndexRequest request){
		
		return  apartmentIndexService.create(request );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable int id){
		
		return  apartmentIndexService.delete(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDTO> payment(@PathVariable int id, @Valid @RequestBody UpdateIndexRequest request) {		
	
		return	apartmentIndexService.update(id, request);
	}
	
	@PutMapping("/payment/{id}")
	public ResponseEntity<ResponseDTO> payment(@PathVariable int id,
												@RequestParam("paid") boolean paid,
												@RequestParam("id_nv") int id_nv) {		
	
		return	apartmentIndexService.payment(id, paid, id_nv);
	}
	
	
	
}
