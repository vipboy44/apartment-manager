package poly.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.PriceParking;
import poly.com.repository.PriceParkingRepository;

import java.util.List;

@Service
public class PriceParkingService {

// < ------------------------------- class Price Parking Service ----------------------------------> 

    @Autowired
    PriceParkingRepository priceParkingRepository;
// --------------------------------------------------------

    // < ------------------------------ find all ------------------------->
    public ResponseEntity<ResponseDTO> findAll() {
        List<PriceParking> priceParkings = priceParkingRepository.findAll();
        return ResponseEntity.ok(new ResponseDTO(priceParkings, null));
    }

    // <-------------------------------- find by Id ------------------------>
    public ResponseEntity<ResponseDTO> findbyId(int id) {
        try {
            PriceParking priceParking = priceParkingRepository.findById(id).orElse(null);
            return ResponseEntity.ok(new ResponseDTO(priceParking, null));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // < -------------------------------- Create ---------------------------------->
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> createPriceParking(PriceParking priceParking) {
        try {
            PriceParking price = priceParkingRepository.findByYearMonthAndLimit(
                    priceParking.getDate().getYear() + 1900,
                    priceParking.getDate().getMonth() + 1,
                    priceParking.getTypeVehicel());
            if (price != null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_PARKING), HttpStatus.CONFLICT);

            priceParking.setId(0);
            priceParking = priceParkingRepository.save(priceParking);
            return ResponseEntity.ok(new ResponseDTO(priceParking, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ----------------------------------- Update -------------------------------- >
    @SuppressWarnings("deprecation")
    public ResponseEntity<ResponseDTO> updatePriceParking(int id, PriceParking priceParking) {

        try {
            if (!priceParkingRepository.existsById(id))
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_PARKING), HttpStatus.NOT_FOUND);

            PriceParking price = priceParkingRepository.findByYearMonthAndLimit(
                    priceParking.getDate().getYear() + 1900,
                    priceParking.getDate().getMonth() + 1,
                    priceParking.getTypeVehicel());
            if (price != null && price.getId() != id)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_PRICE_PARKING), HttpStatus.CONFLICT);

            priceParking.setId(id);
            priceParking = priceParkingRepository.save(priceParking);
            return ResponseEntity.ok(new ResponseDTO(priceParking, MessageSuccess.UPDATE_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < --------------------------------- Delete -----------------------------------> 
    public ResponseEntity<ResponseDTO> deletePriceManagemet(int id) {
        try {
            if (!priceParkingRepository.existsById(id))
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_PARKING), HttpStatus.NOT_FOUND);

            priceParkingRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
