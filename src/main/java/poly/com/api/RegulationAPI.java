package poly.com.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.com.dto.ResponseDTO;
import poly.com.entity.Regulation;
import poly.com.service.RegulationService;

@RestController
@RequestMapping("/api/regulation")
public class RegulationAPI {

	@Autowired
	RegulationService regulationService;


	// < ------------------- findById ----------------------->
	@GetMapping()
	public ResponseEntity<ResponseDTO> findByMaxId() {
		return regulationService.findByMaxId();
	}

    // < ---------------------- Update -------------------------> 
	@PutMapping()
	public ResponseEntity<ResponseDTO> UpdateRegulation(@Valid @RequestBody Regulation regulation) {
		return regulationService.updateRegulation(regulation);
	}
  
}
