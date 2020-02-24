package au.com.new1step.herbalist.security;

import au.com.new1step.herbalist.jpa.model.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DummyContentUtil {
    
    public static final List<AppUser> generateDummyUsers() {
        List<AppUser> appUsers = new ArrayList<>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 /*
        appUsers.add(new AppUser("Lionel Messi", "lionel@messi.com", passwordEncoder.encode("li1234")));
        appUsers.add(new AppUser("Cristiano Ronaldo", "cristiano@ronaldo.com", passwordEncoder.encode("c1234")));
        appUsers.add(new AppUser("Neymar Dos Santos", "neymar@neymar.com", passwordEncoder.encode("n1234")));
        appUsers.add(new AppUser("Luiz Suarez", "luiz@suarez.com", passwordEncoder.encode("lu1234")));
        appUsers.add(new AppUser("Andres Iniesta", "andres@iniesta.com", passwordEncoder.encode("a1234")));
        appUsers.add(new AppUser("Ivan Rakitic", "ivan@rakitic.com", passwordEncoder.encode("i1234")));
        appUsers.add(new AppUser("Ousman Dembele", "ousman@dembele.com", passwordEncoder.encode("o1234")));
        appUsers.add(new AppUser("Sergio Busquet", "sergio@busquet.com", passwordEncoder.encode("s1234")));
        appUsers.add(new AppUser("Gerard Pique", "gerard@pique.com", passwordEncoder.encode("g1234")));
        appUsers.add(new AppUser("Ter Stergen", "ter@stergen.com", passwordEncoder.encode("t1234")));
        */
        return appUsers;
    }


    public static Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            public String getAuthority() {
                return "ROLE_USER";
            }
        };
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
    }

}
