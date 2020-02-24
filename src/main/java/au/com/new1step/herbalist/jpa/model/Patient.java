package au.com.new1step.herbalist.jpa.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Andy Wu on 2/04/2018.
 */


@Entity
@Data
@Table(name = "PATIENT")
public class Patient extends BaseEntity implements Comparable<Patient>{

    @Id
    @Column(name="Patient_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pID;

    @Column(name="First_Name", unique = true)
    @NotEmpty
    private String firstName;

    @Column(name="Last_Name")
    @NotEmpty
    private String lastName;

    @Column(name = "Sex")
    private String sex;

    //   @Column (name="Phone")
    //   @NaturalId --- this field cannot be changed
    @Column (name="Phone", unique = true)  // this field can be change
    @NotEmpty
    private String phone;

    // this field is not used yet
    @Column(name = "Email")
    private String email;

    @Column (name="DOB")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
   // @NotNull -- some patient does not like tell his/her age
    private LocalDate dob;

    @Transient
    private String dobString; // this field not store in DB "2018-09-18"

    @ManyToMany(fetch = FetchType.LAZY,mappedBy="patients")
    public List<Doctor> doctors = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions = new ArrayList<>();

    public void addPrescription(Prescription prescription){
        prescriptions.add(prescription);
        prescription.setPatient(this);
    }

    public void removePrescription(Prescription prescription) {
        prescription.setPatient(null);
        this.prescriptions.remove(prescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(phone, patient.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }

    @Override
    public int compareTo(Patient thePatient){
        return this.lastName.compareTo(thePatient.lastName);
    }

}
