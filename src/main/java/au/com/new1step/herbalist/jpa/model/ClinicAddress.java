package au.com.new1step.herbalist.jpa.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CLINIC_ADDRESS")
@Data
public class ClinicAddress extends BaseEntity implements Comparable<ClinicAddress> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Address_ID")
	private Long addrId;

	@NotNull
	@Column(name="Address1")
	public String address1;

	@Column(name="Address2")
	public String address2;

	@NotNull
	@Column(name="Suburb")
	public String suburb;

	@NotNull
	@Column(name="Postcode")
	public String postCode;

	@NotNull
	@Column(name="State")
	public String state;

	@NotNull
	@Column(name="Country")
	public String country;

	@ManyToOne
	@JoinColumn(name = "Clinic_ID")
	private Clinic clinic;

	@Override
	public int compareTo(ClinicAddress theAddress){
		return this.suburb.compareTo(theAddress.suburb);
	}
}
