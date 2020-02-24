package au.com.new1step.herbalist.jpa.model;

import au.com.new1step.herbalist.validation.ValidEmail;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Andy Wu on 2/04/2018.
 */
@Data
@Entity
@Table(name="DOCTOR")
public class Doctor extends BaseEntity implements Comparable<Doctor> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Doctor_ID")
    private Long drID;

    @Column(name = "First_Name")
    @NotEmpty
    private String firstName;

    @Column(name = "Last_Name")
    @NotEmpty
    private String lastName;

    @Column(name = "Mobile")
    @NotEmpty
    private String mobile;

    @Column(name = "Email")
//     @NotEmpty(message = "Please provide an e-mail")  // many chinese doctors do not have email
 //   @Email(message = "Please provide a valid e-mail")
    @ValidEmail (message = "{ValidEmail.doctorCommand.email}")    // message can omit like NotEmpty
    private String email;

    @Column(name = "Sex")
    private String sex;

/* keep here this parts will need later on

    @Column(name = "Date_Of_Work")
    private String datesOfWork;

    @Column(name = "Business_Hours")
    private String businessHours;
*/

    @Column(name="Default_Fee")
    private Float defaultFee;

    @Column(name="Show_Patient_Page_Size")
    private Integer showPatientPageSize;

    @Column(name = "Print_Chinese_Only")
    private Boolean printChineseNameOnly; // print chinese herb name in prescription

    @Column(name = "copy_pre_prescrition")
    private Boolean copyPrePrescription;

    @Column(name = "Validate_Herb")
    private Boolean validateHerbName; // validate herb against Herb table. if shop set to true, it must verify regardless this field is set true or false

    @Column(name="Herb_Size")
    @Range(min=3, max=30, message="{Range.doctorCommand.herbSize}") // define the message location
                                                                    // if Range.doctorCommand.herbSize existing, it will work like above, otherwise will give default message
    private Integer herbSize; // number of herb displays on prescription

    @Column(name="Prescription_History_Size")
    private Integer showPresHistorySize;

    //  煎服法
    @Column(name = "Medicine_Packages")
    @NotEmpty
    private Integer medicinePackages;
    @Column(name = "Medicine_Days")
    @NotEmpty
    private Integer medicineDays;
    @Column(name = "Water_Cups")
    @NotEmpty
    private String waterCups;
    @Column(name = "Water_Remain")
    @NotEmpty
    private String waterRemain;
    @Column(name = "Drink_Time")
    @NotEmpty
    private String drinkTime;
    @Column(name = "Second_Water_Cups")
    @NotEmpty
    private String secondWaterCups;
    @Column(name = "Second_Water_Remain")
    @NotEmpty
    private String secondWaterRemain;

    @Column(name = "Signature")
    @NotEmpty
    private String signature;



    @ManyToOne
    @JoinColumn(name = "Clinic_ID")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "Address_ID")
    private ClinicAddress clinicAddress;

    @OneToOne
    @JoinColumn(name = "User_ID")
    private AppUser user;


    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="Doctor_Patient",
            joinColumns = @JoinColumn(name="Doctor_ID"),
            inverseJoinColumns = @JoinColumn(name="Patient_ID")
    )
    private List<Patient> patients = new ArrayList<>();

    public void addPatient(Patient patient){
        patients.add(patient);
        patient.getDoctors().add(this);
    }

    // for many to many when remove a patient, it only remove the link between the patient and doctor
    // the patient did not remove as it may link to another doctor.
    public void removePatient(Patient patient){
        patients.remove(patient);
        patient.getDoctors().remove(patient);
    }

    public void removeAllPatients(){
        for(Patient patient : new ArrayList<>(patients)){
            removePatient(patient);
        }
    }


    @Override
    public int compareTo(Doctor theDoctor){
        return this.firstName.compareTo(theDoctor.firstName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        return drID != null && drID.equals(((Doctor) o).drID);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
