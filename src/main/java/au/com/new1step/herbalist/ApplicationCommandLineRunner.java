package au.com.new1step.herbalist;

import au.com.new1step.herbalist.jpa.repositories.GlobalParameterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.sql.DataSource;


@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    @Autowired
    DataSource dataSource;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationCommandLineRunner.class);

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        String url = dataSource.getConnection().getMetaData().getURL();
        logger.info("DATASOURCE url = " + url);
    }

}
