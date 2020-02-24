package au.com.new1step.herbalist.jpa.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="GLOBAL_PARAMETER")
@Data
public class GlobalParameter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "System_ID")
    private Long sID;

    @Column(name="System", unique = true)
    private String system; // = "Herb"; // default value

    @Column(name = "No_Of_Doctor")
    private byte[] noOfDoctor;

    @Column(name="Available_Days")
    private byte[] availableDays;

    @Column(name="No_Of_Clinic")
    private byte[] noOfClinic;

    @Column(name="Start_Date")
    private LocalDate startDate;
    @Column(name="Expired_Date")
    private LocalDate expiredDate;

    public Integer fetchAvailableDays(){
        String num = new String(availableDays);
        return Integer.parseInt(num);
    }

    public Integer fetchNoOfClinic(){
        String num = new String(noOfClinic);
        return Integer.parseInt(num);
    }

    public Integer fetchNoOfDoctor(){
        String num = new String(noOfDoctor);
        return Integer.parseInt(num);
    }

    public String toString(){
        return "System: " + system + ", Number Of Clinics: " + fetchNoOfClinic() + ", Number Of Doctors: " + fetchNoOfDoctor()
                + ", Available Days: " + fetchAvailableDays();
    }
}

