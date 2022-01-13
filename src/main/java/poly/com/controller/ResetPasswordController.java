package poly.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import poly.com.entity.Employee;
import poly.com.service.ResetPasswordService;

@Controller
@RequestMapping("api/account")
public class ResetPasswordController {

    @Autowired
    ResetPasswordService resetPasswordService;

    @GetMapping("/page-forgot-password")
    public ModelAndView displayResetPassword(ModelAndView modelAndView, Employee employee) {
        modelAndView.addObject("employee", employee);
        modelAndView.setViewName("resetpassword/form-check-email");
        return modelAndView;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ModelAndView sendtokentoemail(ModelAndView modelAndView, @RequestParam("email") String email) {
        return resetPasswordService.sendtokentoemail(modelAndView, email);
    }

    @GetMapping("/confirm-reset")
    public ModelAndView validateresettoken(ModelAndView modelAndView, @RequestParam("token") String token) {
        return resetPasswordService.validateresettoken(modelAndView, token);
    }

    // khoi requestParam van duoc mien ten bien trung voi ten param la duoc
    @PostMapping(value = "/reset-password")
    public ModelAndView resetpassword(ModelAndView modelAndView, Employee employee, @RequestParam("token") String token) {
        return resetPasswordService.resetpassword(modelAndView, employee, token);
    }
}
