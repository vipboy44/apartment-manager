package poly.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.PriceElectricity;
import poly.com.repository.PriceElectricityRepository;

@Service
public class PriceElectricityService {

// < ------------------------------- Class PriceElectricity Service ----------------------------->

    @Autowired
    PriceElectricityRepository priceElectricityRepository;
// ----------------------------------------------------------------------

    // < --------------------------- Find All --------------------------->
    public ResponseEntity<ResponseDTO>  findAllElectricity() {
        List<PriceElectricity> priceElectricities = priceElectricityRepository.findAll();
        return ResponseEntity.ok(new ResponseDTO(priceElectricities, null));
    }

    // < --------------------------- Find By Id -------------------------->
    public ResponseEntity<ResponseDTO>  findPriceElectricitybyId(int id) {
        try {
            PriceElectricity priceElectricity = priceElectricityRepository.findById(id).orElse(null);
            return ResponseEntity.ok(new ResponseDTO(priceElectricity, null));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ---------------------------- Create ------------------------------------>
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> createPriceElectricity(PriceElectricity newPriceElectricity) {
        try {
            PriceElectricity priceElectricity = priceElectricityRepository.findByLimit(
                    newPriceElectricity.getDate().getYear() + 1900,
                    newPriceElectricity.getDate().getMonth() + 1,
                    newPriceElectricity.getLimits());
            if (priceElectricity != null)
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_ELECTRICITY), HttpStatus.CONFLICT);

            newPriceElectricity.setId(0);
            newPriceElectricity = priceElectricityRepository.save(newPriceElectricity);
            return ResponseEntity.ok(new ResponseDTO(newPriceElectricity, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ----------------------------- Update ------------------------------------->
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> updatePriceElectricity(int id, PriceElectricity newPriceElectricity) {
        try {
            if (!priceElectricityRepository.existsById(id))
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_ELECTRICITY), HttpStatus.NOT_FOUND);
            
            PriceElectricity priceElectricity = priceElectricityRepository.findByLimit(
                    newPriceElectricity.getDate().getYear() + 1900,
                    newPriceElectricity.getDate().getMonth() + 1,
                    newPriceElectricity.getLimits());
            if (priceElectricity != null && priceElectricity.getId() != id)
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_ELECTRICITY), HttpStatus.CONFLICT);

            newPriceElectricity.setId(id);
            newPriceElectricity = priceElectricityRepository.save(newPriceElectricity);
            return ResponseEntity.ok(new ResponseDTO(newPriceElectricity, MessageSuccess.UPDATE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < --------------------------- Delete ------------------------------------------->
    public ResponseEntity<ResponseDTO> deletePriceElectricity(int id) {
        try {
            if (!priceElectricityRepository.existsById(id))
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_ELECTRICITY), HttpStatus.NOT_FOUND);

            priceElectricityRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

}
