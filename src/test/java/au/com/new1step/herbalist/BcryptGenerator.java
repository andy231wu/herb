package au.com.new1step.herbalist;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/* this is used for super user to generate encripted password */
public class BcryptGenerator {
    public static void main(String[] args) {

        int i = 0;
        while (i < 5) {
            String password = "down5hub";
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);

            System.out.println(hashedPassword);
            i++;
        }

    }
}
