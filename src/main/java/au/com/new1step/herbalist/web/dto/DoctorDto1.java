package au.com.new1step.herbalist.web.dto;

import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import au.com.new1step.herbalist.validation.ValidEmail;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DoctorDto1 {
    private Long drID;

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String mobile;

    @NotEmpty
    @ValidEmail (message = "{ValidEmail.doctorCommand.email}")    // message can omit like NotEmpty
    private String email;

    private String sex;
    private Float defaultFee;

    @NotNull
    private Integer showPatientPageSize;
    private Boolean printChineseNameOnly; // print chinese herb name in prescription
    private Boolean copyPrePrescription;
    private Boolean validateHerbName; // validate herb against Herb table. if shop set to true, it must verify regardless this field is set true or false

    @Range(min=3, max=30, message="{Range.doctorCommand.herbSize}") // define the message location
    //@Range(min=3, max=30)  // if Range.doctorCommand.herbSize existing, it will work like above, otherwise will give default message
    private Integer herbSize; // number of herb displays on prescription

    private Integer showPresHistorySize;

   // private ClinicAddress clinicAddress;
    private Long addrId;;
}
