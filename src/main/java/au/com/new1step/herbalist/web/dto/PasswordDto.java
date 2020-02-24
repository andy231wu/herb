package au.com.new1step.herbalist.web.dto;


import lombok.Data;

@Data
public class PasswordDto {

    private String oldPassword;

   // @ValidPassword
    private String newPassword;


}
