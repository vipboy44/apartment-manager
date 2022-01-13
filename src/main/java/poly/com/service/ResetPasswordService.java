package poly.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import poly.com.entity.Employee;
import poly.com.entity.TokenResetPassword;
import poly.com.repository.EmployeeRepository;
import poly.com.repository.PasswordResetRespository;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class ResetPasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordResetRespository passwordResetRespository;

    @Value("${app.domain}")
    private String domain;


    /* -----------------------Scheduling delete token ----------------------------- */
    // scheduleding(lap lich) het tgian se tu dong xoa  cho token
    @Scheduled(cron = "${purge.cron.expression}")
    public void TOKENEXPIRED() {
        Date now = Date.from(Instant.now());
        passwordResetRespository.deleteAllByExpiryDate(now);
    }

    /* ----------------------------- Reset Password ----------------------*/
    public ModelAndView sendtokentoemail(ModelAndView modelAndView, String email) {
        /*  check exist email */
        try {
            Employee employee = employeeRepository.findByEmail(email).orElse(null);
            if (employee != null) {
                TokenResetPassword existTokenUser = passwordResetRespository.findByEmployee(employee);
                if(existTokenUser != null){
                    passwordResetRespository.deleteById(existTokenUser.getId());
                }
                    /*save it */
                    TokenResetPassword token = new TokenResetPassword(employee);
                    passwordResetRespository.save(token);
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(email);
                    mailMessage.setSubject("Xác nhận đặt lại mật khẩu  ");
                    mailMessage.setFrom("ndt.programmer@gmail.com");
                    mailMessage.setText(
                            "Xin chào Bạn " + "\n"
                                    + "chúng tôi đã nhận được yêu cầu  đặt lại mật khẩu của bạn " + "\n"
                                    + "vui lòng click vào link bên dưới để đặt lại mật khẩu " + "\n"
                                    + domain +  "/api/account/confirm-reset?token="
                                    + token.getToken());
                    emailSenderService.sendEmail(mailMessage);
                    modelAndView.addObject("messageSuccess",
                            "Hệ thống đã gửi cho bạn một e-mail có kèm theo link" + "\n" + " để đặt lại mật khẩu, kiểm tra email của bạn");

                    modelAndView.setViewName("resetpassword/form-check-email");
            } else {/* if email dose not exist return not found */
                modelAndView.addObject("messageError", "Email này không tồn tại, vui lòng kiểm tra lại ");
                modelAndView.setViewName("resetpassword/form-check-email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    /*  ------------------------------------- validateresettoken -----------------------  */
    public ModelAndView validateresettoken(ModelAndView modelAndView, String token) {
        try {
            TokenResetPassword passwordResetToken = passwordResetRespository.findByToken(token);
            if (passwordResetToken != null) {
                Employee employee = employeeRepository.findByEmail(passwordResetToken.getEmployee().getEmail()).orElse(null);
                employeeRepository.save(employee);
                modelAndView.addObject("employee", employee);
                modelAndView.addObject("email", employee.getEmail());
                modelAndView.addObject("token", token);
                modelAndView.setViewName("resetpassword/form-reset-password");
            } else {
                modelAndView.addObject("message", "Liên kết không hợp lệ hoặc bị hỏng!");
                modelAndView.setViewName("404");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    /* ------------------------------------resetpassword ---------------------------*/
    public ModelAndView resetpassword(ModelAndView modelAndView, Employee employee, String token) {
        TokenResetPassword tokenResetPasswrod = passwordResetRespository.findByToken(token);
        if (tokenResetPasswrod == null) {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("404");
        } else {
            Employee tokenEmployee = tokenResetPasswrod.getEmployee();
            tokenEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employeeRepository.save(tokenEmployee);
            modelAndView.addObject("messageSuccess", "Đặt lại mật khẩu thành công");
            modelAndView.setViewName("login/login");
            passwordResetRespository.deleteById(tokenResetPasswrod.getId());
        }
        return modelAndView;
    }
}
