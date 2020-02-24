package au.com.new1step.herbalist.jpa.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Andy Wu on 2/04/2018.
 */
@Entity
@Data
@Table(name = "Herb")
public class Herb extends BaseEntity {
    @Id
    @Column(name="Herb_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hID;

    @NaturalId
    @Column (name="Chinese_Name")
    private String chineseName;

    @NaturalId
    @Column (name="English_Name")
    private String englishName;

    @Column (name="Unit_Price")
    private Float unitPrice;  // setup price for each gram

    @Column (name="Quantity")
    private Integer quantity;

    @Column (name="Cost")
    private Float cost;     // price for above quantity

    @Column (name ="Start_Date")
    private Date startDate;

    @Column (name ="Expire_Date")
    private Date expireDate;

    @ManyToOne
    @JoinColumn(name="Class_ID")
    private HerbClass herbClass;
}
