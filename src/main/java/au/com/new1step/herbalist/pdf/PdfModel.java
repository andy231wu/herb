package au.com.new1step.herbalist.pdf;

import au.com.new1step.herbalist.web.dto.MedicineDto;
import lombok.Data;

import java.util.List;

@Data
public class PdfModel {
    // header
    private String shopChineseName;
    private String shopEnglishName;
    private String shopDescription;
    private String shopAddress;
    private String shopSuburb;
    private String shopState;
    private String shopPostcode;
    private String shopPhone;

    // content
    private String patientName;
    private Integer patientAge;
    private String patientSex;
    private String patientDate;
    private Long preId;

    //List<Medicine> lists;
    private Boolean isOnlyPrintChinese;
    private List<MedicineDto> medicines;

    private String doctorSignature;

    //  煎服法
    private Integer medicinePackages;
    private Integer medicineDays;
    private String waterCups;
    private String waterRemain;
    private String drinkTime;
    private String SecondWaterCups;
    private String SecondWaterRemain;

}
