package poly.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.PriceGarbage;
import poly.com.repository.PriceGarbageRepository;

@Service
public class PriceGarbageService {

// < ------------------------------ Class Price GarbageServices -------------------------------> 

    @Autowired
    PriceGarbageRepository priceGarbageRepository;
// -------------------------------------------------

    // < -------------------- find All ---------------------------->
    public ResponseEntity<ResponseDTO> findPriceGarbageAll() {
        List<PriceGarbage> priceGarbages = priceGarbageRepository.findAll();
        return ResponseEntity.ok(new ResponseDTO(priceGarbages, null));
    }

    // < -------------------- find by Id ---------------------------->
    public ResponseEntity<ResponseDTO>findPriceGarbageById(int id) {
        try {
            PriceGarbage priceGarbage = priceGarbageRepository.findById(id).orElse(null);    
            return ResponseEntity.ok(new ResponseDTO(priceGarbage, null));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ---------------------------- Create ------------------------->
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> createPriceGarbage(PriceGarbage priceGarbage) {
        try {
            PriceGarbage priceGarbages = priceGarbageRepository.findByYearAndMonth(
                            priceGarbage.getDate().getYear() + 1900,
                            priceGarbage.getDate().getMonth() + 1);
            if (priceGarbages != null)
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_GARBAGE), HttpStatus.CONFLICT);

            priceGarbage.setId(0);
            PriceGarbage garbage = priceGarbageRepository.save(priceGarbage);
            return ResponseEntity.ok(new ResponseDTO(garbage, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ----------------------------- update ------------------------- >
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> updatePriceGarbage(int id, PriceGarbage priceGarbage) {
        try {
            // id: priceWater không tồn tại
            if (!priceGarbageRepository.existsById(id))
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_GARBAGE), HttpStatus.NOT_FOUND);

            // Giá các tháng trước đã có
            PriceGarbage priceGarbages = priceGarbageRepository.findByYearAndMonth(
                    priceGarbage.getDate().getYear() + 1900,
                    priceGarbage.getDate().getMonth() + 1);
            if (priceGarbages != null && priceGarbages.getId() != id)
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_GARBAGE), HttpStatus.CONFLICT);

            priceGarbage.setId(id);
            priceGarbage = priceGarbageRepository.save(priceGarbage);
            return ResponseEntity.ok(new ResponseDTO(priceGarbage, MessageSuccess.UPDATE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ----------------------------- Delete -------------------------->
    public ResponseEntity<ResponseDTO> deletePriceGarbage(int id) {
        try {
            if (!priceGarbageRepository.existsById(id))
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_GARBAGE), HttpStatus.NOT_FOUND);
            
            priceGarbageRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
