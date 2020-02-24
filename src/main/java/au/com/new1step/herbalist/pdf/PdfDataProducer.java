package au.com.new1step.herbalist.pdf;

import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import au.com.new1step.herbalist.jpa.model.Doctor;
import au.com.new1step.herbalist.jpa.model.Medicine;
import au.com.new1step.herbalist.jpa.model.Patient;
import au.com.new1step.herbalist.jpa.model.Prescription;
import au.com.new1step.herbalist.service.DoctorService;
import au.com.new1step.herbalist.service.ShopService;
import au.com.new1step.herbalist.web.dto.MedicineDto;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PdfDataProducer {
    private ShopService shopService;
    private DoctorService doctorService;

    public PdfModel fetchPrescriptionData(Long preID) {
        PdfModel data = new PdfModel();
        Clinic shop = shopService.findAllClinics().get(0);
        Doctor doctor = doctorService.findLoginDoctor();
        ClinicAddress address = doctor.getClinicAddress();

        Prescription prescription = doctorService.findPrescriptionById(preID);
        Patient patient = prescription.getPatient();

        data.setShopChineseName(shop.getChineseName());
        data.setShopEnglishName(shop.getEnglishName());
        String addr = address.getAddress1();
        if(!StringUtils.isEmpty(address.getAddress2())){
            addr += " " + address.getAddress2();
        }
        data.setShopAddress(addr);
        data.setShopSuburb(address.getSuburb());
        data.setShopState(address.getState());
        data.setShopPostcode(address.getPostCode());
        data.setShopPhone(shop.getPhone());
        data.setShopDescription(shop.getDescription());

        data.setPatientName(patient.getLastName() + patient.getFirstName());

        LocalDate birthday = patient.getDob();
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthday, now);
        data.setPatientAge(period.getYears());

        data.setPatientSex(patient.getSex());

        Date updateDate = Date.from(prescription.getUpdateDate().atZone(ZoneId.systemDefault()).toInstant());
        data.setPatientDate(DateFormatUtils.format(updateDate, "yyyy-MM-dd"));

        data.setPreId(preID);
        data.setDoctorSignature(doctor.getSignature());

        data.setIsOnlyPrintChinese(doctor.getPrintChineseNameOnly());

        // -- herb ---

        List<Medicine> medicines = prescription.getMedicines();
        List<MedicineDto> medicineDtos = new ArrayList<>();
        MedicineDto medicineDto;
        Medicine medicine;
        for(int i=0; i<medicines.size(); i++) {
            medicine = medicines.get(i);
            medicineDto = new MedicineDto();
            medicineDto.setName1(medicine.getChineseName());
            medicineDto.setEngName1(medicine.getEnglishName());
            medicineDto.setWeight1(medicine.getWeight());

            i++;
            if(i == medicines.size()){
                medicineDtos.add(medicineDto);
                break;
            }
            medicine = medicines.get(i);
            medicineDto.setName2(medicine.getChineseName());
            medicineDto.setEngName2(medicine.getEnglishName());
            medicineDto.setWeight2(medicine.getWeight());

            i++;
            if(i == medicines.size()){
                medicineDtos.add(medicineDto);
                break;
            }
            medicine = medicines.get(i);
            medicineDto.setName3(medicine.getChineseName());
            medicineDto.setEngName3(medicine.getEnglishName());
            medicineDto.setWeight3(medicine.getWeight());

            medicineDtos.add(medicineDto);
        }

        data.setMedicines(medicineDtos);

        //  煎服法
        if(prescription.getMedicinePackages() == 0) {
            data.setMedicinePackages(doctor.getMedicinePackages());
        }else{
            data.setMedicinePackages(prescription.getMedicinePackages());
        }

        if(prescription.getMedicineDays() == 0){
            data.setMedicineDays(doctor.getMedicineDays());
        }else{
            data.setMedicineDays(prescription.getMedicineDays());
        }

        data.setWaterCups(doctor.getWaterCups());
        data.setWaterRemain(doctor.getWaterRemain());
        data.setDrinkTime(doctor.getDrinkTime());
        data.setSecondWaterCups(doctor.getSecondWaterCups());
        data.setSecondWaterRemain(doctor.getSecondWaterRemain());

        return data;
    }

    /* this will remove later */
    public PdfModel demoFetchPrescriptionData() {
        PdfModel data = new PdfModel();
        data.setShopChineseName("悉尼端州中药行");
        data.setShopEnglishName("EASTWOOD CHINESE HERBAL MEDICINE");
        data.setShopAddress("Shop 12, 1 Lakeside Rd");
        data.setShopSuburb("Eastwood");
        data.setShopState("NSW");
        data.setShopPostcode("2122");
        data.setShopPhone("(02) 9874 9353");
        data.setShopDescription("Chinese Herbalist & Acupuncturist");

        data.setPatientName("吴连福");
        data.setPatientAge(55);
        data.setPatientSex("男");
        data.setPatientDate("23/04/2018");
        data.setPreId(888l);
        data.setDoctorSignature("崔医生");

        data.setIsOnlyPrintChinese(true);

        List<MedicineDto> medicines = new ArrayList<>();
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setName1("茯苓");
        medicineDto.setEngName1("Her1");
        medicineDto.setWeight1(1);
        medicineDto.setName2("佛手果");
        medicineDto.setEngName2("Her2");
        medicineDto.setWeight2(2);
        medicineDto.setName3("付海石");
        medicineDto.setEngName3("Her3");
        medicineDto.setWeight3(3);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("葛根");
        medicineDto.setEngName1("Her4");
        medicineDto.setWeight1(4);
        medicineDto.setName2("狗急");
        medicineDto.setEngName2("Her5");
        medicineDto.setWeight2(5);
        medicineDto.setName3("甘遂");
        medicineDto.setEngName3("Her6");
        medicineDto.setWeight3(6);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("白卞豆");
        medicineDto.setEngName1("Her7");
        medicineDto.setWeight1(7);
        medicineDto.setName2("张山");
        medicineDto.setEngName2("Her8");
        medicineDto.setWeight2(8);
        medicineDto.setName3("打造");
        medicineDto.setEngName3("Her9");
        medicineDto.setWeight3(9);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("妲己");
        medicineDto.setEngName1("Her10");
        medicineDto.setWeight1(10);
        medicineDto.setName2("水车前草根");
        medicineDto.setEngName2("Her11");
        medicineDto.setWeight2(11);
        medicineDto.setName3("浙贝母");
        medicineDto.setEngName3("Her12");
        medicineDto.setWeight3(12);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("泽兰");
        medicineDto.setEngName1("Her13");
        medicineDto.setWeight1(13);
        medicineDto.setName2("珍珠");
        medicineDto.setEngName2("Her14");
        medicineDto.setWeight2(14);
        medicineDto.setName3("志可");
        medicineDto.setEngName3("Her15");
        medicineDto.setWeight3(15);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("栀子");
        medicineDto.setEngName1("Her16");
        medicineDto.setWeight1(16);
        medicineDto.setName2("智源智");
        medicineDto.setEngName2("Her17");
        medicineDto.setWeight2(17);
        medicineDto.setName3("知母");
        medicineDto.setEngName3("Her18");
        medicineDto.setWeight3(18);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("朱陵");
        medicineDto.setEngName1("Her19");
        medicineDto.setWeight1(19);
        medicineDto.setName2("竹茹");

        medicineDto.setEngName2("Her20");
        medicineDto.setWeight2(20);
        medicineDto.setName3("苟杞子");
        medicineDto.setEngName3("Her21");
        medicineDto.setWeight3(21);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("钩藤");
        medicineDto.setEngName1("Her22");
        medicineDto.setWeight1(22);
        medicineDto.setName2("狗急");
        medicineDto.setEngName2("Her23");
        medicineDto.setWeight2(23);
        medicineDto.setName3("顾随卜");
        medicineDto.setEngName3("Her24");
        medicineDto.setWeight3(24);
        medicines.add(medicineDto);

        medicineDto = new MedicineDto();
        medicineDto.setName1("瓜娄");
        medicineDto.setEngName1("Her25");
        medicineDto.setWeight1(25);
        medicineDto.setName2("管仲");
        medicineDto.setEngName2("Her26");
        medicineDto.setWeight2(26);
        medicines.add(medicineDto);


        data.setMedicines(medicines);

        //  煎服法
        data.setMedicinePackages(6);
        data.setMedicineDays(6);
        data.setWaterCups("4");
        data.setWaterRemain("3/4");
        data.setDrinkTime("1");
        data.setSecondWaterCups("3");
        data.setSecondWaterRemain("3/4");

        return data;
    }
}
