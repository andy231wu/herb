package au.com.new1step.herbalist.security;

import au.com.new1step.herbalist.jpa.model.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AppUserPrincipal implements UserDetails {

    private final AppUser user;

    public AppUserPrincipal(AppUser user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = Arrays.asList( new SimpleGrantedAuthority( user.getRole() ) );
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        if("admin".equals(user.username)){
            return true;
        }
        return user.isAccountNonExpired();      // account expired
    }

    @Override
    public boolean isAccountNonLocked() {  // if not payment received account can lock
        if("admin".equals(user.username)){
            return true;
        }
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if("admin".equals(user.username)){
            return true;
        }
        return user.isCredentialsNonExpired();                          // reset the password
    }

    @Override
    public boolean isEnabled() {
        if("admin".equals(user.username)){
            return true;
        }
        return user.isEnabled();                   //  if the account create but have not activate
    }


    public AppUser getAppUser() {
        return user;
    }

}
