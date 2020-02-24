package au.com.new1step.herbalist.security;

import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database. Implementation of the User Details Service.
 */
@Service
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService() {
        super();
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Called with username {}", username);

        AppUser appUser = userRepository.findByUsername(username);

        if( appUser == null )
            throw new UsernameNotFoundException( "User not found by name: " + username );

        return new AppUserPrincipal(appUser);


    }

    public void insertUser(AppUser user){
        userRepository.save(user);
    }
}
