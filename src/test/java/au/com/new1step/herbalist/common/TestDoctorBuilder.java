package au.com.new1step.herbalist.common;


import au.com.new1step.herbalist.jpa.model.Doctor;

public class TestDoctorBuilder {

    private Long drID;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String sex;
    private Float defaultFee;
    private Boolean printChineseNameOnly; // print chinese herb name in prescription
    private Boolean validateHerbName; // validate herb against Herb table
    private Integer herbSize; // number of herb displays on prescription

    //  煎服法  
    private Integer medicinePackages; 
    private Integer medicineDays;
    private String waterCups;   
    private String waterRemain;  
    private String drinkTime;    
    private String secondWaterCups; 
    private String secondWaterRemain;
  
    private String signature;

    public TestDoctorBuilder withDrID(Long drID){
        this.drID = drID;
        return this;
    }

    public TestDoctorBuilder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }

    public TestDoctorBuilder withLastName(String lastName){
        this.lastName = lastName;
        return this;
    }

    public TestDoctorBuilder withMobile(String mobile){
        this.mobile = mobile;
        return this;
    }

    public TestDoctorBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public TestDoctorBuilder withSex(String sex){
        this.sex = sex;
        return this;
    }

    public TestDoctorBuilder withDefaultFee(Float defaultFee){
        this.defaultFee = defaultFee;
        return this;
    }

    // ... more fields

    public Doctor build(){
        Doctor doctor = new Doctor();
        doctor.setDrID(drID);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setMobile(mobile);
        doctor.setEmail(email);
        doctor.setSex(sex);
        doctor.setDefaultFee(defaultFee);
        return doctor;
    }
}
