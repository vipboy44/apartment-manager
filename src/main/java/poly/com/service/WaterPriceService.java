
package poly.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.PriceWater;
import poly.com.repository.PriceWaterRepository;

@Service
public class WaterPriceService {


    @Autowired
    PriceWaterRepository priceWaterRepository;
// ---------------------------------------------------------

    // < --------------------------- find All -------------------------->
    public ResponseEntity<ResponseDTO> findAll() {
        List<PriceWater> priceWater = priceWaterRepository.findAll();
        return ResponseEntity.ok(new ResponseDTO(priceWater, null));
    }

    // < -------------------------- find by Id ---------------------------->
    public ResponseEntity<ResponseDTO> findById(int id) {
        try {
            PriceWater water = priceWaterRepository.findById(id).orElse(null);
            return ResponseEntity.ok(new ResponseDTO(water, null));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < --------------------------- Create ---------------------------------->
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> createPriceWater(PriceWater priceWater) {
        try { 
            PriceWater priceWaters = priceWaterRepository
                    .findByYearAndMonth(
                            priceWater.getDate().getYear() + 1900,
                            priceWater.getDate().getMonth() + 1);
            if (priceWaters != null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_WATER), HttpStatus.CONFLICT);

            priceWater.setId(0);
            priceWater = priceWaterRepository.save(priceWater);
            return ResponseEntity.ok(new ResponseDTO(priceWater, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ------------------------------ Update --------------------------------->
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> updatePriceWaterEntity(int id, PriceWater priceWater) {
        try {
            if (!priceWaterRepository.existsById(id))
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_WATER), HttpStatus.NOT_FOUND);

            PriceWater price = priceWaterRepository.findByYearAndMonth(
                    priceWater.getDate().getYear() + 1900,
                    priceWater.getDate().getMonth() + 1);

            if (price != null && id != price.getId())
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_WATER), HttpStatus.CONFLICT);

            priceWater.setId(id);
            priceWater = priceWaterRepository.save(priceWater);
            return ResponseEntity.ok(new ResponseDTO(priceWater, MessageSuccess.UPDATE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ------------------------------- Delete ----------------------------------->
    public ResponseEntity<ResponseDTO> deletePriceWater(int id) {
        try {
            if (!priceWaterRepository.existsById(id))
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_WATER), HttpStatus.NOT_FOUND);

            priceWaterRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}