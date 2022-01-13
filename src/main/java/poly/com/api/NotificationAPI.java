package poly.com.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.com.dto.ResponseDTO;
import poly.com.entity.Notification;
import poly.com.service.NotificationService;


@RestController
@RequestMapping("/api/notification")
public class NotificationAPI {

	@Autowired
    NotificationService  notificationService;
    // ----------------------------------------

    // < ------------------------ findAll ------------------>
    @GetMapping()
    public ResponseEntity<ResponseDTO> findAll() {
        return notificationService.findAll();
    }

    // < ------------------------- findById ---------------------->
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable int id) {
        return notificationService.findById(id);
    }

    // < ----------------------------- Create --------------------->
    @PostMapping()
    public ResponseEntity<ResponseDTO> create(@RequestBody Notification notification) {
        return notificationService.createNotification(notification);
    }

    // < ----------------------------- Update --------------------->
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable int id, @RequestBody Notification notification) {
        return notificationService.updateNotification(id, notification);
    }

    // < ----------------------------- Delete --------------------->
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
        return notificationService.deleteNotification(id);
    }
	
}
