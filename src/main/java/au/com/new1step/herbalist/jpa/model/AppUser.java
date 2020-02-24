package au.com.new1step.herbalist.jpa.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * Created by awu on 11/12/2017.
 */

@Entity
@Table(name="USER")
@Data
public class AppUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long uID;

    @Column(name="Username", unique = true)
    @NotEmpty
    public String username;

    @Column(name="Password", length = 60)
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    @org.springframework.data.annotation.Transient    // so the password will not return to client
    public String password;

    @Column(name="Role")  // admin, owner, doctor
    @NotEmpty
    public String role;

    @Column(name="Last_Login")
    private LocalDate lastLogin;

    @Column(name="Available_Days")
    private byte[] availableDays;     // default value from globalParameter

    @Column(name="Start_Date")
    private LocalDate startDate;

    @Column(name="Expired_Date")
    private LocalDate expiredDate;

    @Column(name="isAccountNonExpired")
    private boolean isAccountNonExpired;  // check if account is expired
    @Column(name="isAccountNonLocked")     // check if account is locked, not payment not received yet
    private boolean isAccountNonLocked;
    @Column(name="isCredentialsNonExpired")  // check if password is expired, if password over a period e.g. you want user to update password
    private boolean isCredentialsNonExpired;
    @Column(name="isEnabled")                  // check if enable after received payment
    private boolean isEnabled;

    public boolean isAccountNonExpired() {
        boolean isNonExpired = true;
        LocalDate today = LocalDate.now();

        String num = new String(availableDays);
        Integer vailDays = Integer.parseInt(num);

        if(today.isAfter(expiredDate) || (vailDays <= 0 && !today.isEqual(lastLogin))){
            isNonExpired = false;
        }

        return isNonExpired;
    }

    public void setMD5Password(String password) {
        this.password = DigestUtils.md5DigestAsHex(password.getBytes() );
    }
    public String getMD5Password() {
        return password;
    }
}
