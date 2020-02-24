package au.com.new1step.herbalist.jpa.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Andy Wu on 2/04/2018.
 */
@Entity
@Data
@Table(name = "Medicine")
public class Medicine extends BaseEntity {
    @Id
    @Column(name="Medicine_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mID;

    @Column (name="Weight")
    private Integer weight;    // unit gram

    @Column (name="Cost") // cost from Herb table = weight * unitPrice
    private Float cost;

    @Column (name="Chinese_Name")
    private String chineseName;    // duplicate from herb table

    @Column (name="English_Name")
    private String englishName;    // duplicate from herb table

    @Column (name="Unit_Price")    // // duplicate from herb table
    private Float unitPrice;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "Herb_ID")
    private Herb herb;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "Prescription_ID")
    private Prescription prescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;
        return mID != null && mID.equals(((Medicine) o).mID);
    }

    @Override
    public int hashCode() {
        return 33;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();


        sb.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        sb.append("mID").append("='").append(mID).append("' ");
        sb.append("weight").append("='").append(weight).append("' ");
        sb.append("cost").append("='").append(cost).append("' ");
        sb.append("chineseName").append("='").append(chineseName).append("' ");
        sb.append("englishName").append("='").append(englishName).append("' ");
        sb.append("unitPrice").append("='").append(unitPrice).append("' ");
        sb.append("herbId").append("='").append(herb.getHID()).append("' ");
        sb.append("createDate").append("='").append(getCreateDate()).append("' ");
        sb.append("updateDate").append("='").append(getUpdateDate()).append("' ");
        sb.append("createUser").append("='").append(getCreateUser()).append("' ");
        sb.append("updateUser").append("='").append(getUpdateUser()).append("' ");
        sb.append("]");

        return sb.toString();

    }

}
