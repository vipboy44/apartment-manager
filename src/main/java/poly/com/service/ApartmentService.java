package poly.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.Apartment;
import poly.com.repository.ApartmentRepository;
import poly.com.repository.OwnApartmentRepository;

@Service
public class ApartmentService {

	@Autowired
	ApartmentRepository apartmentRepository;
	
	@Autowired
	OwnApartmentRepository ownApartmentRepository;
	
	 @Autowired
	 PasswordEncoder passwordEncoder;
	
	 @Value("${password.default}")
	 private String defaultPassword;
	 
    // < -------------------- find All ---------------------------->
    public ResponseEntity<ResponseDTO> findAll() {	
        List<Apartment> apartments = apartmentRepository.findAll();
        return ResponseEntity.ok(new ResponseDTO(apartments, null));
    }

    // < -------------------- find by Id ---------------------------->
    public  ResponseEntity<ResponseDTO> findById(String id) {
        try {
        	Apartment apartment = apartmentRepository.findById(id).orElse(null);    
        	 return ResponseEntity.ok(new ResponseDTO(apartment, null));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ---------------------------- Create ------------------------->
 
    public ResponseEntity<ResponseDTO> createApartment(Apartment apartment) {
        try { 
        	// Id already exists  
        	Apartment apartment2 = apartmentRepository.findById(apartment.getId()).orElse(null);
        	if (apartment2 != null)
        		return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_ID_APARTMENT), HttpStatus.CONFLICT);
        	
        	 if ( apartment.getOwnApartment() != null &&  !ownApartmentRepository.existsById(apartment.getOwnApartment().getId()))		 
                 return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_OWN_APARTMENT), HttpStatus.NOT_FOUND);
        	
        	apartment.setPassword(passwordEncoder.encode(apartment.getPassword()));
        	apartment = apartmentRepository.save(apartment);
        	return ResponseEntity.ok(new ResponseDTO(apartment, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ----------------------------- update ------------------------- 
    public ResponseEntity<ResponseDTO> updateApartment(String id, Apartment newApartment) {
        try {
            Apartment apartment = apartmentRepository.findById(id).orElse(null);
            if (apartment == null)
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_APARTMENT), HttpStatus.NOT_FOUND);
            
            if ( newApartment.getOwnApartment() != null &&  !ownApartmentRepository.existsById(newApartment.getOwnApartment().getId()))
            	 return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_OWN_APARTMENT), HttpStatus.NOT_FOUND);

            newApartment.setId(id);
            newApartment = apartmentRepository.save(newApartment);
            return ResponseEntity.ok(new ResponseDTO(newApartment, MessageSuccess.UPDATE_SUCCSESS));

        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ----------------------------- update ------------------------- 
    public ResponseEntity<ResponseDTO> resetPassword(String id) {
        try {
            Apartment apartment = apartmentRepository.findById(id).orElse(null);
            if (apartment == null)
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_APARTMENT), HttpStatus.NOT_FOUND);
            
            apartment.setPassword(passwordEncoder.encode("12345678"));
            apartmentRepository.save(apartment);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.RESET_PASSWORD_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // < ----------------------------- Delete -------------------------->
    public ResponseEntity<ResponseDTO> deleteApartment(String id) {
        try {
        	Apartment apartment = apartmentRepository.findById(id).orElse(null);
        	if (apartment == null)
        		return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_APARTMENT), HttpStatus.NOT_FOUND);
            if (apartment.getOwnApartment() != null) 
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.DELETE_FAIL), HttpStatus.CONFLICT);	
            
        	 apartmentRepository.deleteById(id);
        	 return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<List<String>> findAllId() {
        try {
             List<String> listId = apartmentRepository.findAllId();
            return new ResponseEntity<>(listId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	
}
