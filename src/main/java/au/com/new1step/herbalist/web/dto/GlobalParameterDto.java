package au.com.new1step.herbalist.web.dto;

import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import lombok.Data;

import javax.persistence.Column;

@Data
public class GlobalParameterDto{
    private Long sID;
    private Integer numberOfDoctor;
    private Integer daysAvailable;
    private Integer numberOfClinic;
    private String startDate;
    private String endDate;
}
