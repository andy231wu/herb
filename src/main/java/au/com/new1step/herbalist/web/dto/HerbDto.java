package au.com.new1step.herbalist.web.dto;

import au.com.new1step.herbalist.jpa.model.HerbClass;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class HerbDto {
        private Long hID;
        private String chineseName;
        private String englishName;
        private Float unitPrice;  // setup price for each
        private Integer quantity;
        private Float cost;     // price for above quantity
        private Date startDate;
        private Date expireDate;
        private HerbClass herbClass;
        private List<HerbClass> herbClasses;
}
