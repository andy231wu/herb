package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.jpa.model.Herb;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class HerbRepositoryTest {
    Logger log = LoggerFactory.getLogger(HerbRepositoryTest.class);

    @Autowired
    HerbRepository herbRepository;

    public void testAddHerb(){
        Herb jensen = ApplicationBuilder.buildHerb("Jensen1", "Jensen2", 10, 50.00f);
        Herb dudong = ApplicationBuilder.buildHerb("DuDong1", "DuDong2", 11, 50.00f);
        Herb shanzhai = ApplicationBuilder.buildHerb("ShanZhai1", "ShanZhai2", 12, 50.20f);

        herbRepository.save(jensen);
        herbRepository.save(dudong);
        herbRepository.save(shanzhai);

        List<Herb> herbs = herbRepository.findAll();
        assertEquals(3, herbs.size());
    }



}
