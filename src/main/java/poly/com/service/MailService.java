package poly.com.service;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.com.dto.Message;

@Service
public class MailService {

   @Autowired
   public JavaMailSender emailSender;
   
public ResponseEntity<Message> sendEmail(  String sendTo, String subject ,String body, MultipartFile[] mFiles) {
	  
	String html = "<html><body><img src='https://drive.google.com/uc?id=18u5te2KfgdFjkbH01JtR27iha0UNOwi4' width='200' hight='200'>"
						+ body + "</html></body>" ;
		try {
			 emailSender.send(new MimeMessagePreparator() {       	 
		            @Override
		            public void prepare(MimeMessage mimeMessage) throws Exception {
		                MimeMessageHelper messageHelper = new MimeMessageHelper(
		                        mimeMessage, true, "UTF-8");     
		                messageHelper.setTo(sendTo);
		                messageHelper.setSubject(subject);
		                messageHelper.setText(html, true);        
		 	                for (MultipartFile multipartFile : mFiles) {
		 	                	if (!multipartFile.isEmpty()) {
		 	                		 messageHelper.addAttachment(multipartFile.getOriginalFilename(), new InputStreamSource() {             
					                        @Override
					                        public InputStream getInputStream() throws IOException {
					                            return multipartFile.getInputStream();
					                        }
					                    });
								}           	
			                }  
						}    
			 });	  
			 return ResponseEntity.ok(new Message("Gửi mail thành công!"));	
		} catch (Exception e) {
			return new ResponseEntity<>(new Message("Gửi mail thất bại!"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		   
   }
   
   
   
   
}
