package poly.com.security.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 6 ,max = 12, message = "Password from 6 to 12 characters!")
    private String password;

    @NotNull
    @Size(min = 6 ,max = 12, message = "Password from 6 to 12 characters!")
    private String newpassword;
}
