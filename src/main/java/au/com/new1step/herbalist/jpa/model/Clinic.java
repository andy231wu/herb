package au.com.new1step.herbalist.jpa.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="CLINIC")
@Data
public class Clinic extends BaseEntity implements Comparable<Clinic>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Clinic_ID")
    private Long cID;

    @Column(name="Chinese_Name", unique = true)
    @NotEmpty
    @Length(max = 64)
    public String chineseName;

    @Column(name="Englist_Name", unique = true)
    @Length(max = 64)
    public String englishName;

    @Column(name="Phone")
    public String phone;

    @Column(name="Email")
    public String email;

    @Column(name="Description")
    public String description;

    @Column(name = "Validate_Herb")
    private Boolean validateHerbName; // request doctor enter a validate herb against Herb table

    @OneToMany(fetch = FetchType.LAZY, mappedBy="clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicAddress> addresses = new ArrayList();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor> doctors = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HerbClass> herbClasses = new ArrayList<>();

    public void addAddress(ClinicAddress address){
        addresses.add(address);
        address.setClinic(this);
    }

    // this method used to delete induvidule address only
    // for bulk delete it usded CascadeType.delete
    public void removeAddress(ClinicAddress address){
        address.setClinic(null); //remove relationship, it become orphan object, so address will remove from db
        addresses.remove(address); // save as above
    }

    public void addDoctor(Doctor doctor){
        doctors.add(doctor);
        doctor.setClinic(this);
    }
    public void removeDoctor(Doctor doctor){
        doctor.setClinic(null);
        doctors.remove(doctor);
    }

    public void addHerbClass(HerbClass herbClass){
        herbClasses.add(herbClass);
        herbClass.setClinic(this);
    }
    public void removeHerbClass(HerbClass herbClass){
        herbClass.setClinic(null);
        herbClasses.remove(herbClass);
    }

    @Override
    public int compareTo(Clinic theClinic){
        return this.chineseName.compareTo(theClinic.chineseName);
    }
}
