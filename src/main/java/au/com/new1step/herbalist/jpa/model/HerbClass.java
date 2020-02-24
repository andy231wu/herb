package au.com.new1step.herbalist.jpa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Wu on 2/04/2018.
 */
@Entity
@Data
@Table(name = "Herb_Class")
public class HerbClass extends BaseEntity {
    @Id
    @Column(name="Class_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cID;

    @Column (name="Name", unique = true)
    private String name;

    @Column (name="Category")
    private String category; // this not need

    @ManyToOne
    @JoinColumn(name = "Clinic_ID")
    private Clinic clinic;

    @OneToMany(mappedBy = "herbClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Herb> herbs = new ArrayList();

    public void addHerb(Herb herb){
        herbs.add(herb);
        herb.setHerbClass(this);
    }

    public void removeHerb(Herb herb){
        herb.setHerbClass(null);
        herbs.remove(herb);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HerbClass)) return false;
        return cID != null && cID.equals(((HerbClass) o).cID);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
