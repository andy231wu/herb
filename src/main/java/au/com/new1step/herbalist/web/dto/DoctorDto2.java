package au.com.new1step.herbalist.web.dto;

import au.com.new1step.herbalist.jpa.model.*;
import au.com.new1step.herbalist.validation.ValidEmail;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Wu on 2/04/2018.
 */
@Data
public class DoctorDto2 {
    private Long drID;

    //  煎服法
    @NotNull
    private Integer medicinePackages;

    @NotNull
    private Integer medicineDays;

    @NotEmpty(message = "ANDY-TEST")
    private String waterCups;

    @NotEmpty
    private String waterRemain;

    @NotEmpty
    private String drinkTime;

    @NotEmpty
    private String secondWaterCups;

    @NotEmpty
    private String secondWaterRemain;
    @NotEmpty
    private String signature;
}
