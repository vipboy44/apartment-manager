package poly.com.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.com.constant.FireBasePhotoFolders;
import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.dto.ResponseDTO;
import poly.com.entity.ERole;
import poly.com.entity.Employee;
import poly.com.entity.Role;
import poly.com.helper.EmployeeExportExcel;
import poly.com.helper.FileHelper;
import poly.com.repository.EmployeeRepository;
import poly.com.repository.PasswordResetRespository;
import poly.com.repository.RoleRepository;
import poly.com.request.EmployeeRequest;
import poly.com.security.request.ChangePasswordRequest;

@Service
public class EmployeeService {

    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    PasswordResetRespository passwordResetRespository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileHelper fileHelper;
    
    @Value("${password.default}")
    private String defaultPassword;

    // <----------------------------------- find All ----------------------------->
    public ResponseEntity<ResponseDTO> findAll() {
        List<Employee> employees = employeeRepository.findAll();

        return ResponseEntity.ok(new ResponseDTO(employees, null));
    }

    // < ------------------------------- findById ------------------------------->
    public ResponseEntity<ResponseDTO> findById(int id) {
        try {
            Employee employee = employeeRepository.findById(id).orElse(null);
            return ResponseEntity.ok(new ResponseDTO(employee, null));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseDTO> findByUsername(String username) {
        try {
            Employee employee = employeeRepository.findByUsername(username).orElse(null);
            return ResponseEntity.ok(new ResponseDTO(employee, null));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // < ---------------------------------------- Insert --------------------------------------------->
    public ResponseEntity<ResponseDTO> insertEmployee(EmployeeRequest signUpRequest) {
        try {
            ResponseEntity<ResponseDTO> reponseConflict = checkConflict(0, signUpRequest.getEmail(), signUpRequest.getUsername(),
                    signUpRequest.getPhone(), signUpRequest.getIdentitycard());

            if (reponseConflict != null)
                return reponseConflict;

            Employee employee = new Employee(0, signUpRequest.getFullName(), signUpRequest.isGender(),
                    signUpRequest.getBirthday(), signUpRequest.getIdentitycard(),
                    signUpRequest.getPhone(), signUpRequest.getAddress(),
                    signUpRequest.getEmail(), signUpRequest.getImage(),
                    signUpRequest.getUsername(),
                    passwordEncoder.encode(signUpRequest.getPassword()), null);

            // ----------------------------- Roles -----------------------------> 
            employee.setRoles(getRoles(signUpRequest.getRoles()));
            employee = employeeRepository.save(employee);

            return ResponseEntity.ok(new ResponseDTO(employee, MessageSuccess.INSERT_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // < ------------------------------------ Update ----------------------------------------->
    public ResponseEntity<ResponseDTO> updateEmployee(int id, EmployeeRequest employeeRequest) {
        try {
            Employee employeeExists = employeeRepository.findById(id).orElse(null);
            if (employeeExists == null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE), HttpStatus.NOT_FOUND);

            // < -----------------  Check conflict ----------------------->
            ResponseEntity<ResponseDTO> reponseConflict = checkConflict(id, employeeRequest.getEmail(), employeeRequest.getUsername(),
                    employeeRequest.getPhone(), employeeRequest.getIdentitycard());
            if (reponseConflict != null)
                return reponseConflict;
            
            // --------------------------------------------------------------
            Employee employee = new Employee(
                    id, employeeRequest.getFullName(),
                    employeeRequest.isGender(), employeeRequest.getBirthday(),
                    employeeRequest.getIdentitycard(), employeeRequest.getPhone(),
                    employeeRequest.getAddress(), employeeRequest.getEmail(),
                    employeeExists.getImage(), employeeRequest.getUsername(),
                    employeeExists.getPassword(), null);

            // ----------------------------- Role ----------------------------->      

            employee.setRoles(getRoles(employeeRequest.getRoles()));
            employee = employeeRepository.save(employee);
            return ResponseEntity.ok(new ResponseDTO(employee, MessageSuccess.UPDATE_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    /// < ------------------------- Reset password --------------------- >
    public ResponseEntity<ResponseDTO> resetPassword(int id) {
        try {
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee == null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE), HttpStatus.NOT_FOUND);
            employee.setPassword(passwordEncoder.encode(defaultPassword));
            employeeRepository.save(employee);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.RESET_PASSWORD_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /// < ------------------------- Delete User --------------------- >
    public ResponseEntity<ResponseDTO> deleteEmployee(int id) {
        try {
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee == null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE), HttpStatus.NOT_FOUND);

            employeeRepository.deleteById(id);
            fileHelper.deleteFile("photo/user" ,employee.getImage());
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // < ------------------------------------ upload image ---------------------------->
    public ResponseEntity<ResponseDTO> uploadFile(MultipartFile mfile, int id) {
        if (mfile.isEmpty())
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_400), HttpStatus.BAD_REQUEST);
        try {
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee == null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE), HttpStatus.NOT_FOUND);
            fileHelper.deleteFile("photo/user" ,employee.getImage());
            String fileName = fileHelper.saveFile(mfile, FireBasePhotoFolders.USER, System.nanoTime()+ "admin" + id);
            employee.setImage(fileName);
            employee = employeeRepository.save(employee);
            return ResponseEntity.ok(new ResponseDTO(employee, MessageSuccess.UPLOAD_FILE_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //< --------------------------- check Conflict------------------------------->
    public ResponseEntity<ResponseDTO> checkConflict(int id, String email, String username, String phone, String iddentitycard) {
        try {
            Employee employee_phone = employeeRepository.findByPhone(phone).orElse(null);
            if (employee_phone != null && employee_phone.getId() != id)
                return ResponseEntity.status(409).body(new ResponseDTO(null, MessageError.ERROR_409_PHONE));

            Employee employee_identiyCard = employeeRepository.findByIdentitycard(iddentitycard).orElse(null);
            if (employee_identiyCard != null && employee_identiyCard.getId() != id)
                return ResponseEntity.status(409).body(new ResponseDTO(null, MessageError.ERROR_409_IDENTICARD));

            Employee employee_username = employeeRepository.findByUsername(username).orElse(null);
            if (employee_username != null && employee_username.getId() != id)
                return ResponseEntity.status(409).body(new ResponseDTO(null, MessageError.ERROR_409_USERNAME));
            Employee employee_email = employeeRepository.findByEmail(email).orElse(null);
            if (employee_email != null && employee_email.getId() != id)
                return ResponseEntity.status(409).body(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE_EMAIL));
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public Set<Role> getRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            return null;
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException());
                        roles.add(adminRole);
                        break;
                    case "user":
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException());
                        roles.add(userRole);
                        break;
                    default:
                        throw new RuntimeException();
                }
            });
            return roles;
        }
    }

    /*------------------------------------------  change Username-------------------------------------*/
    public ResponseEntity<ResponseDTO> changeUsername(String username, String newUsername) {
        try {
          Employee employee = employeeRepository.findByUsername(username).orElse(null);
          if (employee == null) 
        	  return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE), HttpStatus.NOT_FOUND);
          if(employeeRepository.existsByUsername(newUsername) && !username.equals(newUsername)) 
        	  return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_USERNAME), HttpStatus.CONFLICT);
          
        	employee.setUsername(newUsername);
        	employee = employeeRepository.save(employee);
        	
          return ResponseEntity.ok(new ResponseDTO(employee, MessageSuccess.UPDATE_SUCCSESS));
  
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }
    
    /*------------------------------------------  change password -------------------------------------*/
    public ResponseEntity<ResponseDTO> changepassword(ChangePasswordRequest passwordRequest) {
        try {
            Employee employee = employeeRepository.findById(passwordRequest.getId()).orElse(null);
            if (employee == null)
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE), HttpStatus.NOT_FOUND);
            String oldPassword = passwordRequest.getPassword();
            String dbPassword = employee.getPassword();
            if (passwordEncoder.matches(oldPassword, dbPassword)) {
                employee.setPassword(passwordEncoder.encode(passwordRequest.getNewpassword()));
                employee = employeeRepository.save(employee);
                return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.UPDATE_PASSWORD_SUCCSESS));
            } else {
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_EMPLOYEE_PASSWORD), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        /* oldPassword nhận password của client truyền về server ( không mã hóa )
         * dbPassword lấy password từ database ( đã mã hóa )
         * passwordEncoder.matches(Boolean) so sánh 2 chuỗi  oldpassword với dbpassword
         * nếu kết quả passwordEncoder.matches là true  thì mã hóa  lại mật khẩu mới và lưu vào database
         * nếu kết quả passwordEncoder.matches là fale thì trả về status code 404 ,mật khẩu cũ không đúng */
    }

    /* ------------------------ Export to EXCEL ----------------------- */
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachement; filename = Employees.xlsx";
            response.setHeader(headerKey, headerValue);
            List<Employee> employeeList = employeeRepository.findAll();
            EmployeeExportExcel employeeExportExcel = new EmployeeExportExcel(employeeList);
            System.out.println(employeeExportExcel);
            employeeExportExcel.export(response);
            return new ResponseEntity<>(MessageSuccess.EXPORT_SUCCSESS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}