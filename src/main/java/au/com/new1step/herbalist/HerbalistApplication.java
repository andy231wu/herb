package au.com.new1step.herbalist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ServletComponentScan
public class HerbalistApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HerbalistApplication.class, args);
    }
}