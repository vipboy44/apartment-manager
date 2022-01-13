package poly.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.Vehicle;
import poly.com.repository.ResidentRepository;
import poly.com.repository.VehicleRespository;

import java.util.List;

@Service
public class VehicleService {
    // < ------------------------------ Class Vehicle Service ----------------------->
    @Autowired
    ResidentRepository residentRepository;
    @Autowired
    VehicleRespository vehicleRespository;
    // -------------------------------

    //< ----------------------------- FindAll ------------------------->
    public ResponseEntity<ResponseDTO> findAll() {
        List<Vehicle> vehicles = vehicleRespository.findAll();
        return ResponseEntity.ok(new ResponseDTO(vehicles, null));
    }

    // <----------------------------- findById --------------------->
    public ResponseEntity<ResponseDTO> findById(int id) {
        try {
            Vehicle vehicle = vehicleRespository.findById(id).orElse(null);
            return ResponseEntity.ok(new ResponseDTO(vehicle, null));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // < --------------------------- Create -------------------------->
    public ResponseEntity<ResponseDTO> create( Vehicle newVehicle){
       try {
           if (!residentRepository.existsById(newVehicle.getResident().getId()))
               return new ResponseEntity<>(new ResponseDTO(null,MessageError.ERROR_404_RESIDENT), HttpStatus.NOT_FOUND);

           if(newVehicle.getLicensePlates() != ""){
               Vehicle vehicleLicensePlates = vehicleRespository
                       .findByLicensePlates(newVehicle.getLicensePlates()).orElse(null);
               if(vehicleLicensePlates != null)
                   return  new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_VEHICLE_LICENSEPLATES),HttpStatus.CONFLICT);
           }else {
               newVehicle.setLicensePlates(null);
           }
           newVehicle.setId(0);
           newVehicle = vehicleRespository.save(newVehicle);
           return ResponseEntity.ok(new ResponseDTO(newVehicle, MessageSuccess.INSERT_SUCCSESS));
       }catch (Exception e){
    	   return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    // < --------------------------- Update-------------------------->
    public  ResponseEntity<ResponseDTO> update(int id, Vehicle newVehicle){
        try {
            if (!residentRepository.existsById(newVehicle.getResident().getId()))
                return new ResponseEntity<>(new ResponseDTO(null,MessageError.ERROR_404_RESIDENT), HttpStatus.NOT_FOUND);

            if(!vehicleRespository.existsById(id))
                return new  ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_VEHICEL), HttpStatus.NOT_FOUND);

            if(newVehicle.getLicensePlates() != ""){
                Vehicle vehicleLicensePlates = vehicleRespository
                        .findByLicensePlates(newVehicle.getLicensePlates()).orElse(null);
                if(vehicleLicensePlates != null && vehicleLicensePlates.getId() != id )
                    return  new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_VEHICLE_LICENSEPLATES),HttpStatus.CONFLICT);
            }
            else {
                newVehicle.setLicensePlates(null);
            }
            newVehicle.setId(id);
            newVehicle = vehicleRespository.save(newVehicle);
            return ResponseEntity.ok(new ResponseDTO(newVehicle, MessageSuccess.UPDATE_SUCCSESS));
        }catch (Exception e){
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // < --------------------------- Delete-------------------------->
    public  ResponseEntity<ResponseDTO> delete(int id ){
        try {
            if (!vehicleRespository.existsById(id))
            	return new  ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_VEHICEL), HttpStatus.NOT_FOUND);
            vehicleRespository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
