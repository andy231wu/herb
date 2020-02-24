package au.com.new1step.herbalist.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SearchParameterDto {
    @NotEmpty
    private String patientSearchString;
}
