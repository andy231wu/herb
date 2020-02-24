package au.com.new1step.herbalist.jpa.model;

import au.com.new1step.herbalist.common.ProcessingStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Wu on 2/04/2018.
 */
@Entity
@Data
@EqualsAndHashCode
@Table(name = "PRESCRIPTION")
public class Prescription extends BaseEntity {
    @Id
    @Column(name="Prescription_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long psID;

    @Column (name="Symptom")
    private String symptom;

    @Column (name="Fee")
    private Float fee;

    @Column (name="Medicine_Packages")
    private Integer medicinePackages;
    @Column (name="Medicine_Days")
    private Integer medicineDays;

    @Column (name="Processing_Status")
    //@NonNull
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus;

    @ManyToOne
    @JoinColumn(name = "Patient_ID")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "Doctor_ID")
    private Doctor doctor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Medicine> medicines = new ArrayList<>();

    public void addMedicine(Medicine medicine){
        medicines.add(medicine);
        medicine.setPrescription(this);
    }

    public void addMedicines(List<Medicine> theMedicines){
        theMedicines.forEach(m -> addMedicine(m));
    }

    public void removeMedicine(Medicine medicine) {
        medicine.setPrescription(null);
        this.medicines.remove(medicine);
    }
    public void removeMedicines(List<Medicine> theMedicines){
        theMedicines.stream().forEach(m -> {
            if(m != null) {
                removeMedicine(m);
            }
        });
    }

}
