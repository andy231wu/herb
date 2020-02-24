package au.com.new1step.herbalist.web.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrescriptionDto {
    private Long presId;
    private Integer medicinePackages;
    private Integer medicineDays;
    private List<HerbItemDto> herbItems = new ArrayList<>();

    // these are temp fields only
    private Integer herbSize;
    private String selectHerbCaption;
}
