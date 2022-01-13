package poly.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.PriceManagement;
import poly.com.repository.PriceManagementRepository;

@Service
public class PriceManagementService {

// < ----------------------------- Class Price Management Service ------------------------->

    @Autowired
    PriceManagementRepository priceManagementRepository;
// < ---------------------------------------------------

    // < ---------------------------- find All ------------------------------->
    public ResponseEntity<ResponseDTO> findAll() {
        List<PriceManagement> priceManagements = priceManagementRepository.findAll();
        return ResponseEntity.ok(new ResponseDTO(priceManagements, null));
    }

    // < ----------------------------- find by Id ------------------------------ >
    public ResponseEntity<ResponseDTO>findbyId(int id) {
        try {
            PriceManagement priceManagement = priceManagementRepository.findById(id).orElse(null);
            return ResponseEntity.ok(new ResponseDTO(priceManagement, null));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ------------------------------ Create --------------------------------->
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> createPriceManagement(PriceManagement priceManagement) {
        try {
            PriceManagement priceManagements = priceManagementRepository.findByYearAndMonth(
                    priceManagement.getDate().getYear() + 1900,
                    priceManagement.getDate().getMonth() + 1);
            if (priceManagements != null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_MANAGEMENT), HttpStatus.CONFLICT); 

            priceManagement.setId(0);
            priceManagement = priceManagementRepository.save(priceManagement);
            return ResponseEntity.ok(new ResponseDTO(priceManagement, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // <-------------------------------- Update --------------------------------->
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> updatePriceManagement(int id, PriceManagement priceManagement) {
        try {
            if (!priceManagementRepository.existsById(id))
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_MANAGEMENT), HttpStatus.NOT_FOUND);

            // Giá các tháng trước đã có
            PriceManagement priceManagements = priceManagementRepository.findByYearAndMonth( priceManagement.getDate().getYear() + 1900,
							                                             priceManagement.getDate().getMonth() + 1);
            if (priceManagements != null && id != priceManagements.getId())
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_MANAGEMENT), HttpStatus.CONFLICT); 

            priceManagement.setId(id);
            priceManagement = priceManagementRepository.save(priceManagement);
            return ResponseEntity.ok(new ResponseDTO(priceManagement, MessageSuccess.UPDATE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ------------------------------- Delete ----------------------------------->
    public ResponseEntity<ResponseDTO> deletePriceManagemet(int id) {
        try {
            if (!priceManagementRepository.existsById(id))
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_MANAGEMENT), HttpStatus.NOT_FOUND);
            
            priceManagementRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
