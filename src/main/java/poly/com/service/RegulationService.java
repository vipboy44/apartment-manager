package poly.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.Regulation;
import poly.com.repository.RegulationRepository;

@Service
public class RegulationService {
	
	@Autowired
	RegulationRepository regulationRepository;
	
	
	// < ---------------------- findById -------------------------->
	
	public ResponseEntity<ResponseDTO> findByMaxId() {
		try {
			Regulation regulation = regulationRepository.findTopByOrderByIdDesc().orElse(null);	
			return ResponseEntity.ok(new ResponseDTO(regulation,null));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// < ------------------------- Update ----------------------------------> 
		public ResponseEntity<ResponseDTO> updateRegulation(Regulation regulation) {
			try {
				List<Regulation> list = regulationRepository.findAll();
				if (list.size() < 1) {
					regulation = regulationRepository.save(regulation);
				}else {
					Regulation regulation2 = regulationRepository.findTopByOrderByIdDesc().orElse(null);	
					if (regulation2 == null)
						return new ResponseEntity<>(new ResponseDTO(regulation,MessageError.ERROR_404_REGULATION), HttpStatus.NOT_FOUND);
					
					regulation.setId(regulation2.getId());
					regulation = regulationRepository.save(regulation);		
				}

				return ResponseEntity.ok(new ResponseDTO(regulation,MessageSuccess.UPDATE_SUCCSESS));
			} catch (Exception e) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}


	
}
