package au.com.new1step.herbalist.web.dto;

import lombok.Data;

@Data
public class SymptomDto {
    private String symptom;
    private Long patientID;
    private Long presID;
}
