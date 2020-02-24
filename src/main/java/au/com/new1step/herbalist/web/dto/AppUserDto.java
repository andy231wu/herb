package au.com.new1step.herbalist.web.dto;

import au.com.new1step.herbalist.jpa.model.BaseEntity;
import lombok.Data;
import org.springframework.util.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by awu on 11/12/2017.
 */
@Data
public class AppUserDto {

    public String username;
    public String password;
    public String role;

    private LocalDate lastLogin;
    private Integer availableDays;
    private LocalDate startDate;
    private LocalDate expiredDate;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
}
