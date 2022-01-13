package poly.com.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /* ------------------------------------ WebSecurityConfig -------------------------------- */

    @Autowired
   UserDetailServiceImpl userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /* ----------------------------------------------------------------------- */

    @Override /* ------------cung cap account cho spring security  -----------------*/
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }


    @Bean /*  ------------------ config CorsOrigin ---------------------- */
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://ad-vinhomegrandparkvn.ap-southeast-1.elasticbeanstalk.com"); /* chi cho phep domain nay gui request*/
        configuration.addAllowedHeader("*");
        /* cho phep cac request method gui request len server */
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override  /* --------------- configure HttpSecurity --------------- */
    protected void configure(HttpSecurity http) throws Exception {
    	http.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400); 
        http.csrf().disable();
        http.headers().cacheControl();    // khong cho browser tu dong cache
       
        http
        	.cors()
        	.and()
        	.authorizeRequests()   	 	
        	.antMatchers("/assets/**","/api/account/*").permitAll()
            .antMatchers("/quan-ly/hoa-don/**","/quan-ly/bang-gia/**").hasAnyRole("USER")
            .antMatchers("/quan-ly/nhan-vien", "/quan-ly/can-ho", "/quan-ly/chu-can-ho").hasAnyRole("ADMIN")    
        	.anyRequest().authenticated() // tat cac request khac  phai duoc xac thuc
            .and()                
        	.formLogin()                                       // cho phep nguoi dung xac thuc bang form login
           		.loginPage("/authentication/account/login").permitAll()// cho phep truy cap trang login
           		.loginProcessingUrl("/login")                      // url login
           		.usernameParameter("username")                     // username
           		.passwordParameter("password")                     // password
           		.defaultSuccessUrl("/quan-ly")                            //WelcomeController
           	.and()    
           		.exceptionHandling().accessDeniedPage("/403")
        	.and()
           	.logout()                                          // cho phep dang xuat
           		.invalidateHttpSession(true)                       //S Hủy session của người dùng
           		.clearAuthentication(true)                         //-------------------
           		.deleteCookies("JSESSIONID")                       //  xoa JSESSIOIND  khi logout success
           		.logoutUrl("/logout")                              //  url logout
           		.logoutSuccessUrl("/authentication/account/login").permitAll(); // dang xuat thanh cong ve trang login

    }
       

}