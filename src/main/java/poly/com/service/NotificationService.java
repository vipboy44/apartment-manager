package poly.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.Notification;
import poly.com.repository.NotificationRepository;

@Service
public class NotificationService {
	@Autowired
	NotificationRepository notificationRepository;
	
	
	// ---------------------------------------------------------

    // < --------------------------- find All -------------------------->
    public ResponseEntity<ResponseDTO> findAll() {
        List<Notification> notifications = notificationRepository.findAll();
        return ResponseEntity.ok(new ResponseDTO(notifications, null));
    }

    // < -------------------------- find by Id ---------------------------->
    public ResponseEntity<ResponseDTO> findById(int id) {
        try {
        	Notification notification = notificationRepository.findById(id).orElse(null);
        	return ResponseEntity.ok(new ResponseDTO(notification, null));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < --------------------------- Create ---------------------------------->
 
    public ResponseEntity<ResponseDTO> createNotification(Notification notification) {
        try {
            notification.setId(0);
            notification = notificationRepository.save(notification);
            return ResponseEntity.ok(new ResponseDTO(notification, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ------------------------------ Update --------------------------------->
  
    public ResponseEntity<ResponseDTO> updateNotification(Integer id,Notification notification) {
        try {
        	Notification notification2 = notificationRepository.findById(id).orElse(null);
        	if (notification2 == null)
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_NOTIFICATION), HttpStatus.NOT_FOUND);
        	
            notification.setId(id);
            notification = notificationRepository.save(notification);
            return ResponseEntity.ok(new ResponseDTO(notification, MessageSuccess.UPDATE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ------------------------------- Delete ----------------------------------->
    public ResponseEntity<ResponseDTO> deleteNotification(int id) {
        try {
            if (!notificationRepository.existsById(id))
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_NOTIFICATION), HttpStatus.NOT_FOUND);
            
            notificationRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
        	return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
