package au.com.new1step.herbalist.web.dto;

import au.com.new1step.herbalist.jpa.model.HerbClass;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class HerbItemDto {
        private String chineseName;
        private String englishName;
        private Integer gram;
}
